package ncd.fileviewer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

public class FileViewerDetails extends TableView<FileDetails> implements IFileViewer {

	private ObservableList<FileDetails>	list	= FXCollections.observableArrayList();

	private String						path;

	@SuppressWarnings("unchecked")
	public FileViewerDetails(String path, ScrollPane scrollPane) {
		this.update(path);

		TableColumn<FileDetails, String> nameCol = new TableColumn<>("Name");
		TableColumn<FileDetails, String> dateCol = new TableColumn<>("Date modified");
		TableColumn<FileDetails, String> typeCol = new TableColumn<>("Type");
		TableColumn<FileDetails, String> sizeCol = new TableColumn<>("Size");

		nameCol.setCellValueFactory(new PropertyValueFactory<FileDetails, String>("name"));
		dateCol.setCellValueFactory(new PropertyValueFactory<FileDetails, String>("date"));
		typeCol.setCellValueFactory(new PropertyValueFactory<FileDetails, String>("type"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<FileDetails, String>("size"));

		getColumns().addAll(nameCol, dateCol, typeCol, sizeCol);

		this.setItems(list);

		this.setRowFactory(tv -> {
			TableRow<FileDetails> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					FileDetails rowData = row.getItem();
					File file = rowData.getFile();
					if (file.isDirectory()) {
						FileEvent<File> e = new FileEvent<>(file);
						fireEvent(e);
					} else {
						new Thread(() -> {
							try {
								Desktop.getDesktop().open(file);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}).start();
					}
				}
			});
			return row;
		});

		prefHeightProperty().bind(scrollPane.heightProperty());
		prefWidthProperty().bind(scrollPane.widthProperty());
	}

	@Override
	public void update(String path) {
		this.list.clear();
		this.path = path;
		File file = new File(path);
		File[] files = file.listFiles();
		Comparator<FileDetails> comparator = this.getComparator();
		if (comparator == null) {
			Arrays.sort(files, new NormalComparator());
		}

		List<FileDetails> fileDetails = new ArrayList<>();
		for (File f : files) {
			try {
				FileDetails fileDetail = new FileDetails(f);
				fileDetails.add(fileDetail);
			} catch (IOException e) {
				continue;
			}
		}
		if (comparator != null) {
			FileDetails[] arr = (FileDetails[]) fileDetails.toArray(new FileDetails[fileDetails.size()]);
			Arrays.sort(arr, comparator);
			this.list = FXCollections.observableArrayList(Arrays.asList(arr));
		} else {
			this.list = FXCollections.observableArrayList(fileDetails);
		}
		this.setItems(this.list);
	}

	@Override
	public Region getRegion() {
		return this;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public void reset() {
		this.list.clear();
		File file = new File(path);
		File[] files = file.listFiles();
		Arrays.sort(files, new NormalComparator());

		for (File f : files) {
			try {
				FileDetails fileDetail = new FileDetails(f);
				this.list.add(fileDetail);
			} catch (IOException e) {
				continue;
			}
		}

	}
}
