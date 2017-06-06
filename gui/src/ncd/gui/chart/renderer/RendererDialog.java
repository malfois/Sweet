package ncd.gui.chart.renderer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ncd.gui.chart.configuration.Renderer;
import ncd.gui.chart.configuration.Renderers;
import ncd.gui.chart.configuration.Stroke;
import ncd.gui.chart.configuration.Symbol;
import ncd.gui.component.ToolTipText;
import ncd.gui.component.initialvalue.CButton;
import ncd.gui.component.initialvalue.CComboBox;
import ncd.gui.component.initialvalue.CDeterminateCheckBox;
import ncd.gui.component.initialvalue.CIndeterminateCheckBox;
import ncd.gui.component.initialvalue.CLabel;
import ncd.gui.component.initialvalue.ColorChooser;
import ncd.gui.component.initialvalue.IComponent;
import ncd.gui.component.initialvalue.RowPane;
import ncd.gui.component.number.ParameterField;
import ncd.gui.event.ComponentEvent;
import ncd.gui.file.FileSelection;
import ncd.parameter.DoubleParameter;
import ncd.parameter.DoubleRange;
import ncd.parameter.IParameter;
import ncd.utils.colour.Palette1D;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.AFile;
import ncd.utils.file.Filters;
import ncd.utils.file.Metadata;
import ncd.utils.file.MetadataType;
import ncd.utils.file.Xml;

public class RendererDialog extends Dialog<DialogParameter> {

	protected DialogParameter	parameter;

	private DefaultTreeView		treeView;

	private RowPane				showAllLines;
	private RowPane				showAllSymbols;

	private LinePane			line;
	private SymbolPane			symbol;

	@SuppressWarnings("unchecked")
	public RendererDialog(String title, DialogParameter parameter) {
		this.parameter = parameter;
		setTitle(title);

		List<MetadataRenderer> list = (List<MetadataRenderer>) parameter.getMetadataRenderers();
		this.treeView = new DefaultTreeView(list);
		RendererGraphic graphic = this.treeView.getSelectedRenderer();
		this.line = new LinePane(graphic.getRenderer().getLine());
		this.symbol = new SymbolPane(graphic.getRenderer().getSymbol());
		this.line.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> lineChanged((ComponentEvent<Stroke>) event));
		this.symbol.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> symbolChanged((ComponentEvent<Symbol>) event));

		Boolean[][] allVisible = this.areAllVisible(parameter.getRenderers());
		showAllLines = new RowPane(new CLabel("Show all lines"), new CIndeterminateCheckBox(allVisible[0]));
		this.showAllLines.setWidths(150, 50);
		showAllSymbols = new RowPane(new CLabel("Show all symbols"), new CIndeterminateCheckBox(allVisible[1]));
		this.showAllSymbols.setWidths(150, 50);
		this.showAllLines.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> allLinesVisible((ComponentEvent<Boolean[]>) event));
		this.showAllSymbols.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> allSymbolsVisible((ComponentEvent<Boolean[]>) event));

		GridPane grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(10, 10, 10, 10));

		grid.add(this.treeView, 0, 0, 1, 2);
		grid.add(this.showAllLines, 0, 2);
		grid.add(this.showAllSymbols, 0, 3);
		grid.add(this.line, 1, 0);
		grid.add(this.symbol, 1, 1);

		ButtonType save = new ButtonType("Save");
		ButtonType load = new ButtonType("Load");
		getDialogPane().getButtonTypes().addAll(save, load, ButtonType.APPLY, ButtonType.CANCEL);
		getDialogPane().setContent(grid);

		SelectionDesign model = (SelectionDesign) this.treeView.getSelectionModel();
		model.selectedTreeItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> oldValue, TreeItem<String> newValue) {
				if (newValue == null) { return; }
				Renderer renderer = ((RendererGraphic) newValue.getGraphic()).getRenderer();
				line.update(renderer.getLine());
				symbol.update(renderer.getSymbol());
			}
		});

		Button buttonLoad = (Button) getDialogPane().lookupButton(load);
		buttonLoad.setTooltip(new ToolTipText("Load the configuration from a file"));

		Button buttonApply = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
		buttonApply.setTooltip(new ToolTipText("Save the configuration in the default file and apply the changes to the plot"));

		Button buttonCancel = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
		buttonCancel.setTooltip(new ToolTipText("Close the dialog without applying any changes"));

		Button buttonSave = (Button) getDialogPane().lookupButton(save);
		buttonSave.setTooltip(new ToolTipText("Save the configuration in a user defined file and apply the changes to the plot"));

		buttonLoad.addEventFilter(ActionEvent.ACTION, event -> {
			FileSelection fileSelection = new FileSelection(parameter.getFileConfiguration(), buttonSave, "Load configuration", Filters.Xml());
			FileConfiguration fileConfiguration = fileSelection.openSingleFile();
			Xml<Renderers> rendererFile = new Xml<>(fileConfiguration, new Renderers());
			rendererFile.read();
			Renderers renderersConf = rendererFile.getConfiguration();

			List<MetadataRenderer> metadata = this.parameter.getMetadataRenderers();
			for (MetadataRenderer mdata : metadata) {
				mdata.initialiseRenderer(renderersConf);
			}
			this.parameter = new DialogParameter(fileConfiguration, metadata);
			this.treeView.clear();
			this.treeView.initialise(metadata);
			event.consume();
		});

		setResultConverter(dialogButton -> {
			if (dialogButton == save) {
				FileSelection fileSelection = new FileSelection(this.parameter.getFileConfiguration(), buttonSave, "Save configuration", Filters.Xml());
				FileConfiguration fileConfiguration = fileSelection.save();
				System.out.println("Save clicked " + fileConfiguration);
				this.saveConfiguration(fileConfiguration);
				return this.parameter;
			}
			if (dialogButton == ButtonType.APPLY) {
				this.saveConfiguration(this.parameter.getFileConfiguration());
				return this.parameter;
			}
			return null;
		});
	}

	private void symbolChanged(ComponentEvent<Symbol> event) {
		CTreeItem item = (CTreeItem) this.treeView.getSelectionModel().getSelectedItems().get(0);
		Renderer renderer = this.parameter.getRenderer(item.getIndex());
		renderer.setSymbol(event.getResult());
		Boolean[][] allVisible = this.areAllVisible(parameter.getRenderers());
		CIndeterminateCheckBox checkBox = (CIndeterminateCheckBox) this.showAllSymbols.getComponent(1);
		checkBox.setValue(allVisible[1]);
		item.setGraphic(renderer.getGraphic());
	}

	private void lineChanged(ComponentEvent<Stroke> event) {
		CTreeItem item = (CTreeItem) this.treeView.getSelectionModel().getSelectedItems().get(0);
		Renderer renderer = this.parameter.getRenderer(item.getIndex());
		renderer.setLine(event.getResult());
		Boolean[][] allVisible = this.areAllVisible(parameter.getRenderers());
		CIndeterminateCheckBox checkBox = (CIndeterminateCheckBox) this.showAllLines.getComponent(1);
		checkBox.setValue(allVisible[0]);
		item.setGraphic(renderer.getGraphic());
	}

	private void saveConfiguration(FileConfiguration fileConfiguration) {
		Renderers renderersConf = new Renderers();
		renderersConf.updateRenderer(this.parameter.getRenderers());
		Xml<Renderers> rendererFile = new Xml<>(fileConfiguration, renderersConf);
		rendererFile.write();
		this.parameter.setFileConfiguration(fileConfiguration);
	}

	private void allLinesVisible(ComponentEvent<Boolean[]> event) {
		Boolean[] visible = event.getResult();
		this.treeView.setLinesVisible(this.treeView.getRoot(), visible[1]);
		List<Renderer> renderers = this.parameter.getRenderers();
		for (Renderer renderer : renderers) {
			renderer.getLine().setVisible(visible[1]);
		}
		CTreeItem item = (CTreeItem) this.treeView.getSelectionModel().getSelectedItems().get(0);
		Renderer renderer = this.parameter.getRenderer(item.getIndex());
		this.line.update(renderer.getLine());
	}

	private void allSymbolsVisible(ComponentEvent<Boolean[]> event) {
		Boolean[] visible = event.getResult();
		this.treeView.setSymbolsVisible(this.treeView.getRoot(), visible[1]);
		List<Renderer> renderers = this.parameter.getRenderers();
		for (Renderer renderer : renderers) {
			renderer.getSymbol().setVisible(visible[1]);
		}
		CTreeItem item = (CTreeItem) this.treeView.getSelectionModel().getSelectedItems().get(0);
		Renderer renderer = this.parameter.getRenderer(item.getIndex());
		this.symbol.update(renderer.getSymbol());
	}

	public Boolean[][] areAllVisible(List<Renderer> list) {
		int nRenderer = list.size();
		int allLineVisible = 0;
		int allSymbolVisible = 0;
		for (Renderer renderer : list) {
			if (renderer.getLine().isVisible()) {
				allLineVisible++;
			}
			if (renderer.getSymbol().isVisible()) {
				allSymbolVisible++;
			}
		}
		Boolean[] allLines = new Boolean[] { new Boolean(true), new Boolean(true) };
		if (allLineVisible == 0) {
			allLines = new Boolean[] { new Boolean(false), new Boolean(false) };
		}
		if (allLineVisible == nRenderer) {
			allLines = new Boolean[] { new Boolean(false), new Boolean(true) };
		}

		Boolean[] allSymbol = new Boolean[] { new Boolean(true), new Boolean(true) };
		if (allSymbolVisible == 0) {
			allSymbol = new Boolean[] { new Boolean(false), new Boolean(false) };
		}
		if (allSymbolVisible == nRenderer) {
			allSymbol = new Boolean[] { new Boolean(false), new Boolean(true) };
		}

		return new Boolean[][] { allLines, allSymbol };
	}

	private class SymbolPane extends TitledPane {

		private Symbol							symbol;

		private RendererRow<Color>				fillPane;
		private RendererRow<String>				typePane;
		private RendererRow<IParameter<Double>>	lengthPane;
		private RendererRow<Boolean>			visiblePane;
		private CButton							reset	= new CButton("Reset");

		@SuppressWarnings("unchecked")
		public SymbolPane(Symbol symbol) {
			this.symbol = symbol;

			this.setCollapsible(false);
			this.setText("Symbol configuration");
			this.fillPane = new RendererRow<>("Color", new ColorChooser(Palette1D.toColor(symbol.getColor())));
			this.typePane = new RendererRow<>("Type", new TypeChooser(this.symbol.getName()));

			Double width = new Double(this.symbol.getLength());
			DoubleRange minimum = new DoubleRange(0.0);
			minimum.setMinimum(0.0);
			DoubleRange maximum = new DoubleRange(10.0);
			maximum.setMinimum(0.0);
			DoubleParameter param = new DoubleParameter(width, minimum, maximum);
			this.lengthPane = new RendererRow<>("Width", new ParameterField<Double>(param));

			this.visiblePane = new RendererRow<>("Visible", new CDeterminateCheckBox(symbol.isVisible()));

			this.fillPane.setWidths(60, 150, 100);
			this.typePane.setWidths(60, 150, 100);
			this.lengthPane.setWidths(60, 150, 100);
			this.visiblePane.setWidths(60, 150, 100);

			VBox box = new VBox(5);
			box.setAlignment(Pos.CENTER);
			box.getChildren().addAll(this.typePane, this.fillPane, this.lengthPane, this.visiblePane, new Separator(), this.reset);

			this.setContent(box);

			BooleanBinding b = Bindings.or(this.fillPane.getEnableProperty(), this.typePane.getEnableProperty());
			b = Bindings.or(b, this.lengthPane.getEnableProperty());
			b = Bindings.or(b, this.visiblePane.getEnableProperty());
			this.reset.disableProperty().bind(b.not());

			this.typePane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeType((ComponentEvent<String>) event));
			this.fillPane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeColor((ComponentEvent<Color>) event));
			this.lengthPane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeWidth((ComponentEvent<IParameter<Double>>) event));
			this.visiblePane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeVisibility((ComponentEvent<Boolean>) event));
			this.reset.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> reset((ComponentEvent<String>) event));

		}

		private void reset(ComponentEvent<String> event) {
			event.consume();
			this.fillPane.reset();
			this.symbol.setColor(Palette1D.TO_RGB(this.fillPane.getValue()));
			this.symbol.setStrokeColor(Palette1D.TO_RGB(this.fillPane.getValue()));
			this.typePane.reset();
			this.symbol.setName(this.typePane.getValue());
			this.visiblePane.reset();
			this.symbol.setVisible(this.visiblePane.getValue());
			this.lengthPane.reset();
			this.symbol.setLength(this.lengthPane.getValue().getValue().doubleValue());
			ComponentEvent<Symbol> e = new ComponentEvent<Symbol>(ComponentEvent.INPUT_VALIDATED, this.symbol);
			this.fireEvent(e);
		}

		private void changeType(ComponentEvent<String> event) {
			this.symbol.setName(event.getResult());
			event.consume();
			ComponentEvent<Symbol> e = new ComponentEvent<Symbol>(ComponentEvent.INPUT_VALIDATED, this.symbol);
			this.fireEvent(e);
		}

		private void changeVisibility(ComponentEvent<Boolean> event) {
			this.symbol.setVisible(event.getResult());
			event.consume();
			ComponentEvent<Symbol> e = new ComponentEvent<Symbol>(ComponentEvent.INPUT_VALIDATED, this.symbol);
			this.fireEvent(e);
		}

		private void changeWidth(ComponentEvent<IParameter<Double>> event) {
			this.symbol.setLength(event.getResult().getValue().doubleValue());
			event.consume();
			ComponentEvent<Symbol> e = new ComponentEvent<Symbol>(ComponentEvent.INPUT_VALIDATED, this.symbol);
			this.fireEvent(e);
		}

		private void changeColor(ComponentEvent<Color> event) {
			this.symbol.setColor(Palette1D.TO_RGB(event.getResult()));
			this.symbol.setStrokeColor(Palette1D.TO_RGB(event.getResult()));
			event.consume();
			ComponentEvent<Symbol> e = new ComponentEvent<Symbol>(ComponentEvent.INPUT_VALIDATED, this.symbol);
			this.fireEvent(e);
		}

		protected void update(Symbol symbol) {
			this.symbol = symbol;
			this.fillPane.setValue(Palette1D.toColor(symbol.getColor()));
			this.typePane.setValue(symbol.getName());
			IParameter<Double> parameter = this.lengthPane.getValue();
			parameter.setValue(symbol.getLength());
			this.lengthPane.setValue(parameter);
			this.visiblePane.setValue(symbol.isVisible());
		}

	}

	private class LinePane extends TitledPane {

		private RendererRow<Color>					colorPane;
		private RendererRow<ObservableList<Double>>	stylePane;
		private RendererRow<IParameter<Double>>		widthPane;
		private RendererRow<Boolean>				visiblePane;
		private CButton								reset	= new CButton("Reset");

		private Stroke								line;

		@SuppressWarnings("unchecked")
		public LinePane(Stroke line) {
			this.line = line;

			this.setCollapsible(false);
			this.setText("Line configuration");

			ObservableList<Double> dashArray = FXCollections.observableArrayList();
			ObservableList<Double> initialDashArrayValue = line.getObservableStyle();
			for (Double d : initialDashArrayValue) {
				dashArray.add(new Double(d));
			}

			this.colorPane = new RendererRow<>("Color", new ColorChooser(Palette1D.toColor(line.getColor())));
			this.stylePane = new RendererRow<>("Style", new DashLineChooser(dashArray));

			Double width = new Double(this.line.getWidth());
			DoubleRange minimum = new DoubleRange(0.0);
			minimum.setMinimum(0.0);
			DoubleRange maximum = new DoubleRange(10.0);
			maximum.setMinimum(0.0);
			DoubleParameter param = new DoubleParameter(width, minimum, maximum);
			this.widthPane = new RendererRow<>("Width", new ParameterField<Double>(param));

			this.visiblePane = new RendererRow<>("Visible", new CDeterminateCheckBox(line.isVisible()));

			this.colorPane.setWidths(60, 150, 50);
			this.stylePane.setWidths(60, 150, 50);
			this.widthPane.setWidths(60, 150, 50);
			this.visiblePane.setWidths(60, 150, 50);

			VBox box = new VBox(5);
			box.setAlignment(Pos.CENTER);

			box.getChildren().addAll(colorPane, this.stylePane, this.widthPane, this.visiblePane, new Separator(), this.reset);

			this.setContent(box);

			BooleanBinding b = Bindings.or(this.colorPane.getEnableProperty(), this.stylePane.getEnableProperty());
			b = Bindings.or(b, this.widthPane.getEnableProperty());
			b = Bindings.or(b, this.visiblePane.getEnableProperty());
			this.reset.disableProperty().bind(b.not());

			this.colorPane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeColor((ComponentEvent<Color>) event));
			this.stylePane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeStyle((ComponentEvent<ObservableList<Double>>) event));
			this.widthPane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeWidth((ComponentEvent<IParameter<Double>>) event));
			this.visiblePane.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeVisibility((ComponentEvent<Boolean>) event));
			this.reset.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> reset((ComponentEvent<String>) event));
		}

		protected void update(Stroke line) {
			this.line = line;
			this.colorPane.setValue(Palette1D.toColor(line.getColor()));
			this.stylePane.setValue(line.getObservableStyle());
			IParameter<Double> parameter = this.widthPane.getValue();
			parameter.setValue(line.getWidth());
			this.widthPane.setValue(parameter);
			this.visiblePane.setValue(line.isVisible());
		}

		private void reset(ComponentEvent<String> event) {
			event.consume();
			this.colorPane.reset();
			this.line.setColor(Palette1D.toText(this.colorPane.getValue()));
			this.stylePane.reset();
			this.line.setStyle(this.stylePane.getValue());
			this.visiblePane.reset();
			this.line.setVisible(this.visiblePane.getValue());
			this.widthPane.reset();
			this.line.setWidth(this.widthPane.getValue().getValue().doubleValue());
			ComponentEvent<Stroke> e = new ComponentEvent<Stroke>(ComponentEvent.INPUT_VALIDATED, this.line);
			this.fireEvent(e);
		}

		private void changeVisibility(ComponentEvent<Boolean> event) {
			this.line.setVisible(event.getResult());
			event.consume();
			ComponentEvent<Stroke> e = new ComponentEvent<Stroke>(ComponentEvent.INPUT_VALIDATED, this.line);
			this.fireEvent(e);
		}

		private void changeStyle(ComponentEvent<ObservableList<Double>> event) {
			this.line.setStyle(event.getResult());
			event.consume();
			ComponentEvent<Stroke> e = new ComponentEvent<Stroke>(ComponentEvent.INPUT_VALIDATED, this.line);
			this.fireEvent(e);
		}

		private void changeColor(ComponentEvent<Color> event) {
			this.line.setColor(Palette1D.toText(event.getResult()));
			event.consume();
			ComponentEvent<Stroke> e = new ComponentEvent<Stroke>(ComponentEvent.INPUT_VALIDATED, this.line);
			this.fireEvent(e);
		}

		private void changeWidth(ComponentEvent<IParameter<Double>> event) {
			this.line.setWidth(event.getResult().getValue());
			event.consume();
			ComponentEvent<Stroke> e = new ComponentEvent<Stroke>(ComponentEvent.INPUT_VALIDATED, this.line);
			this.fireEvent(e);
		}

	}

	private class DefaultTreeView extends TreeView<String> {

		private CTreeItem	selectedItem;
		private CTreeItem	rootNode	= new CTreeItem("Data files");

		public DefaultTreeView(List<MetadataRenderer> parameters) {
			this.setRoot(rootNode);
			rootNode.setExpanded(true);

			this.setSelectionModel(new SelectionDesign(this.getSelectionModel(), this));

			this.initialise(parameters);
		}

		public void initialise(List<MetadataRenderer> parameters) {
			for (MetadataRenderer parameter : parameters) {
				CTreeItem node = this.addData(parameter);
				rootNode.getChildren().add(node);
			}

			SelectionDesign model = (SelectionDesign) this.getSelectionModel();
			if (this.selectedItem == null) {
				model.select(1);
			} else {
				model.select(this.selectedItem);
			}

			int index = this.getSelectionModel().getSelectedIndex();
			this.scrollTo(index);

		}

		public CTreeItem addData(MetadataRenderer parameter) {
			if (parameter.getChildren().isEmpty()) {
				Integer index = Integer.parseInt(parameter.getHeaders().get(MetadataType.CURVE_INDEX));
				CTreeItem node = new CTreeItem(parameter.getHeaders().get(AFile.TITLE), parameter.getRenderer().getGraphic(), index);
				Boolean selected = Boolean.parseBoolean(parameter.getHeaders().get(MetadataType.SELECTED));
				if (selected) {
					this.selectedItem = node;
				}
				return node;
			}

			CTreeItem node = new CTreeItem(parameter.getHeaders().get(AFile.TITLE));
			node.setExpanded(true);
			List<Metadata> metadataList = parameter.getChildren();
			for (Metadata metadata : metadataList) {
				MetadataRenderer param = (MetadataRenderer) metadata;
				CTreeItem item = this.addData(param);
				node.getChildren().add(item);
			}
			return node;
		}

		public void clear() {
			this.rootNode.getChildren().clear();
		}

		public void setLinesVisible(TreeItem<String> root, Boolean visible) {
			for (TreeItem<String> child : root.getChildren()) {
				if (child.isLeaf()) {
					RendererGraphic graphic = (RendererGraphic) child.getGraphic();
					Renderer renderer = graphic.getRenderer();
					renderer.getLine().setVisible(visible);
					graphic.setRenderer(renderer);
				} else {
					setLinesVisible(child, visible);
				}
			}
		}

		public void setSymbolsVisible(TreeItem<String> root, Boolean visible) {
			for (TreeItem<String> child : root.getChildren()) {
				if (child.isLeaf()) {
					RendererGraphic graphic = (RendererGraphic) child.getGraphic();
					Renderer renderer = graphic.getRenderer();
					renderer.getSymbol().setVisible(visible);
					graphic.setRenderer(renderer);
				} else {
					setSymbolsVisible(child, visible);
				}
			}
		}

		public RendererGraphic getSelectedRenderer() {
			SelectionDesign model = (SelectionDesign) this.getSelectionModel();
			TreeItem<String> item = model.getSelectedItems().get(0);
			return (RendererGraphic) item.getGraphic();
		}

	}

	private class CTreeItem extends TreeItem<String> {

		private Integer index;

		public CTreeItem(String text, Node renderer, int index) {
			super(text, renderer);
			this.index = index;
		}

		public CTreeItem(String text) {
			super(text);
		}

		public Integer getIndex() {
			return index;
		}

	}

	private class RendererRow<T> extends RowPane {

		private BooleanProperty enabled = new SimpleBooleanProperty();

		@SuppressWarnings("unchecked")
		public RendererRow(String title, IComponent<T> component) {
			super(new CLabel(title), component, new CButton("Reset"));

			this.enabled.set(false);

			this.getButton().disableProperty().bind(this.enabled.not());

			this.getButton().addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> reset((ComponentEvent<T>) event));

			this.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> changeValue());

			this.setAlignment(Pos.CENTER_LEFT);
		}

		private void changeValue() {
			IComponent<T> component = this.getComponent();
			T initialValue = component.getInitialValue();
			if (initialValue.equals(component.getValue())) {
				this.enabled.set(false);
			} else {
				this.enabled.set(true);
			}
		}

		public BooleanProperty getEnableProperty() {
			return this.enabled;
		}

		public void setValue(T value) {
			this.getComponent().setValue(value);
		}

		public T getValue() {
			return this.getComponent().getValue();
		}

		@SuppressWarnings("unchecked")
		private IComponent<T> getComponent() {
			return (IComponent<T>) this.getComponent(1);
		}

		public CButton getButton() {
			return (CButton) this.getComponent(2);
		}

		@SuppressWarnings("unchecked")
		private void reset(ComponentEvent<T> event) {
			event.consume();
			this.getComponent(1).reset();
			this.enabled.set(false);
			ComponentEvent<T> e = new ComponentEvent<T>(ComponentEvent.INPUT_VALIDATED, (T) this.getComponent(1).getValue());
			this.fireEvent(e);
		}

		protected void reset() {
			this.getComponent(1).reset();
			this.enabled.set(false);
		}

	}

	private class SelectionDesign extends MultipleSelectionModel<TreeItem<String>> {

		private final MultipleSelectionModel<TreeItem<String>>	selectionModel;
		private final TreeView<String>							tree;

		public SelectionDesign(MultipleSelectionModel<TreeItem<String>> selectionModel, TreeView<String> tree) {
			this.selectionModel = selectionModel;
			this.tree = tree;
			selectionModeProperty().bindBidirectional(selectionModel.selectionModeProperty());
		}

		public final ReadOnlyObjectProperty<TreeItem<String>> selectedTreeItemProperty() {
			return this.selectionModel.selectedItemProperty();
		}

		@Override
		public ObservableList<Integer> getSelectedIndices() {
			return selectionModel.getSelectedIndices();
		}

		@Override
		public ObservableList<TreeItem<String>> getSelectedItems() {
			return selectionModel.getSelectedItems();
		}

		@Override
		public void selectIndices(int index, int... indices) {

			List<Integer> indicesToSelect = Stream.concat(Stream.of(index), IntStream.of(indices).boxed()).filter(i -> tree.getTreeItem(i).getGraphic() != null)
					.collect(Collectors.toList());

			if (indicesToSelect.isEmpty()) { return; }
			selectionModel.selectIndices(indicesToSelect.get(0), indicesToSelect.stream().skip(1).mapToInt(Integer::intValue).toArray());

		}

		@Override
		public void selectAll() {
			List<Integer> indicesToSelect = IntStream.range(0, tree.getExpandedItemCount()).filter(i -> tree.getTreeItem(i).getGraphic() != null).boxed()
					.collect(Collectors.toList());
			if (indicesToSelect.isEmpty()) { return; }
			selectionModel.selectIndices(0, indicesToSelect.stream().skip(1).mapToInt(Integer::intValue).toArray());
		}

		@Override
		public void selectFirst() {
			IntStream.range(0, tree.getExpandedItemCount()).filter(i -> tree.getTreeItem(i).getGraphic() != null).findFirst().ifPresent(selectionModel::select);
		}

		@Override
		public void selectLast() {
			IntStream.iterate(tree.getExpandedItemCount() - 1, i -> i - 1).limit(tree.getExpandedItemCount()).filter(i -> tree.getTreeItem(i).getGraphic() != null).findFirst()
					.ifPresent(selectionModel::select);
		}

		@Override
		public void clearAndSelect(int index) {
			int toSelect = index;
			int direction = selectionModel.getSelectedIndex() < index ? 1 : -1;
			while (toSelect >= 0 && toSelect < tree.getExpandedItemCount() && !(tree.getTreeItem(toSelect).getGraphic() != null)) {
				toSelect = toSelect + direction;
			}
			if (toSelect >= 0 && toSelect < tree.getExpandedItemCount()) {
				selectionModel.clearAndSelect(toSelect);
			}
		}

		@Override
		public void select(int index) {
			int toSelect = index;
			int direction = selectionModel.getSelectedIndex() < index ? 1 : -1;
			while (toSelect >= 0 && toSelect < tree.getExpandedItemCount() && !(tree.getTreeItem(toSelect).getGraphic() != null)) {
				toSelect = toSelect + direction;
			}
			if (toSelect >= 0 && toSelect < tree.getExpandedItemCount()) {
				selectionModel.select(toSelect);
			}
		}

		@Override
		public void select(TreeItem<String> obj) {
			if (obj.getGraphic() != null) {
				selectionModel.select(obj);
			}
		}

		@Override
		public void clearSelection(int index) {
			selectionModel.clearSelection(index);
		}

		@Override
		public void clearSelection() {
			selectionModel.clearSelection();
		}

		@Override
		public boolean isSelected(int index) {
			return selectionModel.isSelected(index);
		}

		@Override
		public boolean isEmpty() {
			return selectionModel.isEmpty();
		}

		@Override
		public void selectPrevious() {
			int current = selectionModel.getSelectedIndex();
			if (current > 0) {
				IntStream.iterate(current - 1, i -> i - 1).limit(current).filter(i -> tree.getTreeItem(i).getGraphic() != null).findFirst().ifPresent(selectionModel::select);
			}
		}

		@Override
		public void selectNext() {
			int current = selectionModel.getSelectedIndex();
			if (current < tree.getExpandedItemCount() - 1) {
				IntStream.range(current + 1, tree.getExpandedItemCount()).filter(i -> tree.getTreeItem(i).getGraphic() != null).findFirst().ifPresent(selectionModel::select);
			}
		}

	}

	private class DashLineChooser extends CComboBox<ObservableList<Double>> implements IComponent<ObservableList<Double>> {

		public DashLineChooser(ObservableList<Double> initialValue) {
			super(initialValue);

			this.setCellFactory(listView -> new DashLineCell());
			this.setButtonCell(new DashLineCell());

			ObservableList<ObservableList<Double>> lines = RendererUtility.listOfDashLines();

			this.setItems(lines);
			if (this.getInitialValue() == null) {
				this.setInitialValue(lines.get(0));
			}
			this.setValue(initialValue);

		}

		@Override
		public Control getControl() {
			return this;
		}

		private class DashLineCell extends ListCell<ObservableList<Double>> {

			public DashLineCell() {
			}

			@Override
			protected void updateItem(final ObservableList<Double> item, final boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setGraphic(null);
				} else {
					Stroke line = new Stroke(Color.BLACK);
					line.setLength(100.0);

					if (!item.isEmpty()) {
						line.setStyle(item);
					}
					setGraphic(line.getGraphic());
				}
			}
		}

	}

	private class TypeChooser extends CComboBox<String> implements IComponent<String> {

		public TypeChooser(String initialValue) {
			super(initialValue);

			this.setCellFactory(listView -> new TypeCell());
			this.setButtonCell(new TypeCell());

			ObservableList<String> symbols = RendererUtility.ListOfSymbolNames();

			this.setItems(symbols);
			if (this.getInitialValue() == null) {
				this.setInitialValue(symbols.get(0));
			}
			this.setValue(initialValue);

		}

		@Override
		public Control getControl() {
			return this;
		}

		private class TypeCell extends ListCell<String> {

			public TypeCell() {
			}

			@Override
			protected void updateItem(final String item, final boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setGraphic(null);
				} else {
					setText(item);
					Symbol symbol = new Symbol();
					setGraphic(symbol.getGraphic());
				}
			}
		}

	}

}
