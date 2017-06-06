package ncd.gui.component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class ToolTipText extends Tooltip {

	public ToolTipText(String text) {
		super(text);
		this.setStyle("-fx-background-color: #FFFFCC; -fx-text-fill:black;-fx-font-size: 12px;");
		this.setupCustomTooltipBehavior(250, 10000, 200);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setupCustomTooltipBehavior(int openDelayInMillis, int visibleDurationInMillis, int closeDelayInMillis) {
		try {

			Class TTBehaviourClass = null;
			Class<?>[] declaredClasses = Tooltip.class.getDeclaredClasses();
			for (Class c : declaredClasses) {
				if (c.getCanonicalName().equals("javafx.scene.control.Tooltip.TooltipBehavior")) {
					TTBehaviourClass = c;
					break;
				}
			}
			if (TTBehaviourClass == null) {
				// abort
				return;
			}
			Constructor constructor = TTBehaviourClass.getDeclaredConstructor(Duration.class, Duration.class, Duration.class, boolean.class);
			if (constructor == null) {
				// abort
				return;
			}
			constructor.setAccessible(true);
			Object newTTBehaviour = constructor.newInstance(new Duration(openDelayInMillis), new Duration(visibleDurationInMillis), new Duration(closeDelayInMillis), false);
			if (newTTBehaviour == null) {
				// abort
				return;
			}
			Field ttbehaviourField = Tooltip.class.getDeclaredField("BEHAVIOR");
			if (ttbehaviourField == null) {
				// abort
				return;
			}
			ttbehaviourField.setAccessible(true);

			// Cache the default behavior if needed.
			// Object defaultTTBehavior = ttbehaviourField.get(Tooltip.class);
			ttbehaviourField.set(Tooltip.class, newTTBehaviour);

		} catch (Exception e) {
			System.out.println("Aborted setup due to error:" + e.getMessage());
		}
	}
}
