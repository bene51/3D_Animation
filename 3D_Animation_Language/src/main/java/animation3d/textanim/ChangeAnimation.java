package animation3d.textanim;

import java.util.List;

import animation3d.parser.NumberOrMacro;

public class ChangeAnimation extends Animation {

	public static final int ALL_CHANNELS = 1000;

	private int[] timelineIndices;
	private NumberOrMacro[] vTos;
	private int channel;

	/**
	 * If channel < 0, it's a non-channel property
	 */
	public ChangeAnimation(int fromFrame, int toFrame, int channel, int timelineIdx, NumberOrMacro vTo) {
		super(fromFrame, toFrame);
		this.channel = channel;
		this.timelineIndices = new int[] { timelineIdx };
		this.vTos = new NumberOrMacro[] { vTo };
	}

	public ChangeAnimation(int fromFrame, int toFrame, int channel, int[] timelineIdx, NumberOrMacro[] vTo) {
		super(fromFrame, toFrame);
		this.channel = channel;
		this.timelineIndices = timelineIdx;
		this.vTos = vTo;
	}

	@Override
	protected NumberOrMacro[] getNumberOrMacros() {
		return vTos;
	}

	/**
	 * t = current.getFrame
	 *
	 * If t >  to,               return previous value
	 * If t <  from,             return previous value
	 * If t == from,
	 *            if t == to,    return tgt value
	 *            else           interpolate
	 * If t >  from and t <= to: interpolate
	 *
	 *
	 * interpolate: return linear interpolation of (fromFrame, vFrom, toFrame, vTo),
	 *              where vFrom is the value at fromFrame.
	 */
	@Override
	public void adjustRenderingState(RenderingState current, List<RenderingState> previous, int nChannels) {
		int t = current.getFrame();
		if(t < fromFrame || t > toFrame)
			return;

		int[] channels = new int[] {channel};
		if(channel == ALL_CHANNELS) {
			channels = new int[nChannels];
			for(int c = 0; c < nChannels; c++)
				channels[c] = c;
		}

		for(int ch : channels) {
			for(int i = 0; i < timelineIndices.length; i++) {
				int timelineIdx = timelineIndices[i];
				NumberOrMacro vTo = vTos[i];

				// if it's a macro, just set the value to the macro evaluation
				if(vTo.isMacro()) {
					setRenderingStateProperty(current, timelineIdx, ch, vTo.evaluateMacro(t, fromFrame, toFrame));
					continue;
				}

				double valFrom = -1;
				double valTo = vTo.getValue();
				// otherwise, let's see if there exists a value at fromFrame; if not
				// just use the same value as the target value, unless it's t = fromFrame
				if(t == fromFrame)
					valFrom = getRenderingStateProperty(current, timelineIdx, ch);
				else {
					RenderingState kfFrom = previous.get(fromFrame);
					valFrom = getRenderingStateProperty(kfFrom, timelineIdx, ch);
				}

				// gives precedence to valTo
				setRenderingStateProperty(current, timelineIdx, ch, super.interpolate(current.getFrame(), valFrom, valTo));
			}
		}
	}

	private double getRenderingStateProperty(RenderingState rs, int property, int channel) {
		if(channel < 0)
			return rs.getNonChannelProperty(property);
		return rs.getChannelProperty(channel, property);
	}

	private void setRenderingStateProperty(RenderingState rs, int property, int channel, double v) {
		if(channel < 0)
			rs.setNonChannelProperty(property, v);
		else
			rs.setChannelProperty(channel, property, v);
	}
}
