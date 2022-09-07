package de.cubbossa.splinelib.phase;

import de.cubbossa.splinelib.interpolate.Interpolator;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@Setter
public abstract class CurveBuilderPhase<C extends Interpolator<I, O>, I, O, V> {

	private C interpolator = null;
	private Predicate<V> filter = null;
	private Consumer<O> processor = null;

	public abstract O applyInterpolation(I input, C interpolator, boolean pathClosed);

	public abstract O applyFilter(O input, Predicate<V> filter);

	public abstract O applyProcessor(O input, Consumer<O> processor);

	public O build(I input, boolean closed) {
		O result = applyInterpolation(input, interpolator, closed);
		result = applyFilter(result, filter);
		result = applyProcessor(result, processor);
		return result;
	}
}
