package animation2;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import ij.gui.GenericDialog;


public class AnimatorDialog extends GenericDialog {

	private static final long serialVersionUID = 1L;

	private ActionListener listener;
	private List<HistogramSlider> HistogramSliders = new ArrayList<HistogramSlider>();
	private List<DoubleSlider> doubleSliders = new ArrayList<DoubleSlider>();
	private List<TimelineSlider> timelineSliders = new ArrayList<TimelineSlider>();
	private int cIdx = 0;
	private int sliderIdx = 0;
	private Button okButton;

	public static interface Listener {
		public void renderingSettingsChanged();
		public void nearfarChanged();
		public void boundingBoxChanged();
	}

	public AnimatorDialog(String title) {
		super(title);
	}

	public AnimatorDialog(String title, Frame parent) {
		super(title, parent);
	}

	public void setActionListener(ActionListener l) {
		this.listener = l;
	}

	@Override
	public void showDialog() {
		super.showDialog();
		Button[] buttons = getButtons();
		okButton = buttons[0];
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton && listener != null)
			listener.actionPerformed(e);
		else
			super.actionPerformed(e);
	}

	public HistogramSlider getNextHistogramSlider() {
		return HistogramSliders.get(cIdx++);
	}

	public List<HistogramSlider> getHistogramSliders() {
		return HistogramSliders;
	}

	public DoubleSlider getNextDoubleSlider() {
		return doubleSliders.get(sliderIdx);
	}

	public List<DoubleSlider> getDoubleSliders() {
		return doubleSliders;
	}

	public void addChoice(String label, String[] choice) {
		super.addChoice(label, choice, choice[0]);
	}

	public HistogramSlider addHistogramSlider(String label, int[] histo8, Color color, double min, double max, RenderingSettings r, int nChannels) {
		HistogramSlider slider = new HistogramSlider(histo8, color, min, max, r, nChannels);
		HistogramSliders.add(slider);

		GridBagLayout layout = (GridBagLayout)getLayout();
		GridBagConstraints c = getConstraints();

		if(label != null) {
			Label theLabel = new Label(label);
			c.gridx = 0;
			c.anchor = GridBagConstraints.EAST;
			c.gridwidth = 1;
			layout.setConstraints(theLabel, c);
			add(theLabel);
			c.gridx++;
		}
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		layout.setConstraints(slider, c);
		add(slider);
		return slider;
	}

	public DoubleSlider addDoubleSlider(String label, int[] realMinMax, int[] setMinMax, Color color) {
		DoubleSlider slider = new DoubleSlider(realMinMax, setMinMax, color);
		doubleSliders.add(slider);

		GridBagLayout layout = (GridBagLayout)getLayout();
		GridBagConstraints c = getConstraints();

		if(label != null) {
			Label theLabel = new Label(label);
			c.gridx = 0;
			c.anchor = GridBagConstraints.EAST;
			c.gridwidth = 1;
			layout.setConstraints(theLabel, c);
			add(theLabel);
			c.gridx++;
		}
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		layout.setConstraints(slider, c);
		add(slider);
		return slider;
	}

	public TimelineSlider addTimelineSlider(CtrlPoints ctrls, int current) {
		final TimelineSlider timeline = new TimelineSlider(ctrls, current);
		timelineSliders.add(timeline);

		GridBagLayout layout = (GridBagLayout)getLayout();
		GridBagConstraints c = getConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
//		c.weighty = 1.0;
		layout.setConstraints(timeline, c);
		add(timeline);
		return timeline;
	}

	protected GridBagConstraints getConstraints() {
		GridBagLayout layout = (GridBagLayout)getLayout();
		Panel panel = new Panel();
		addPanel(panel);
		GridBagConstraints constraints = layout.getConstraints(panel);
		remove(panel);
		return constraints;
	}

	public static void main(String[] args) {
//		int[] histo = new int[256];
//		for(int i = 0; i < 256; i++)
//			histo[i] = i;
//		AnimatorDialog gd = new AnimatorDialog("GenericDialogOpener Test");
//		gd.addHistogramSlider("range", histo, Color.GREEN, 1, 4, 1, 4, 1);
//		gd.addNumericField("lkjl", 0, 3);
//		gd.showDialog();
	}
}