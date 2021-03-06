package essentials;

import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * 
 *
 * A slightly changed version of EnhancedOptionPane which has changeable button
 * texts. <br><br>Example:
 * <code>JEnhancedOptionPane.showInputDialog("Number:", new Object[]{"Yes", "No"});</code>
 *
 * @author Maximilian von Gaisberg
 */

public class EnhancedOptionPane extends JOptionPane {
	
	private static final long serialVersionUID = -444186248294917230L;

	public static String showInputDialog(final Object message,
			final Object[] options) throws HeadlessException {
		final JOptionPane pane = new JOptionPane(message, QUESTION_MESSAGE,
				OK_CANCEL_OPTION, null, options, null);
		pane.setWantsInput(true);
		pane.setComponentOrientation(getRootFrame().getComponentOrientation());
		pane.setMessageType(QUESTION_MESSAGE);
		pane.selectInitialValue();
		final JDialog dialog = pane.createDialog(null, UIManager.getString("OptionPane.inputDialogTitle", null));
		dialog.setVisible(true);
		dialog.dispose();
		final Object value = pane.getInputValue();
		return (value == UNINITIALIZED_VALUE) ? null : (String) value;
	}
}
