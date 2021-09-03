# SplineLib

A library for particle path and shape creation in Minecraft

TODO erklärendes Bild

## Usage

### Splines and Terminology

The term "spline" is used to describe smoothing and interpolating functions. In this library, "spline" explicitly means
curves that are defined by any amount of control points. So all those examples are splines:

TODO images

In the context of this library splines are simply lists of BezierVectors. A BezierVector is a child class of Bukkit's
Vector class and contains two further Vectors: leftControlPoint and rightControlPoint. In order to define Bézier curves
controlpoints are obligatory.

TODO image controlpoints

### Shapes

Shapes are objects that return a list of BezierVectors. They accept parameters like a pose and a radius. The Pose class
is a combination of a position vector, a direction vector and an up vector. It defines the position and facing direction
in 3D space.

Predefines Shapes can be found in the Shapes class:

```java
Shapes.rectangle(pose,sizeX,sizeY);
Shapes.star(pose,spikes,smoothing,innerRadius,outerRadius);
```

### Builder

#### Instantiating

The basic way to get a list of vectors from a spline is by using the SplineBuider class. It accepts either a list of
BezierVectors or a Shape (Presets can be found in Shapes class).

```java
SplineBuilder builer = new SplineBuiler(Shapes.star(pose,spikes,smoothing,innerRadius,outerRadius));
```

The `.build()` method returns a list of vectors. In this case it would simply contain all BezierVectors that were placed
in the constructor.

#### Rounding Interpolator

To interpolate points and achieve smoothing you will need to provide a rounding interpolator. The library contains
several predefined rounding interpolators that can be found in the Interpolation class.

You also can create one by implementing the RoundingInterpolator class.

Define it like so:

```java
builder.withRoundingInterpolator(Interpolation.bezierInterpolation(sampling));
```

The sampling parameter defines how many points the interpolator will set. When using the linear interpolator this may
also be your final result to build.

#### Spacing Interpolators

Bezier algorithms have different point spacing depending on the steepness of the curve. To achieve equidistant points
you will need to use a spacing interpolator as well. The sampling of the bezier algorithm defines the smoothness of the
curve, while the spacing interpolator only moves points along the sampled curve. Setting a very low sample resolution
before using space interpolators will lead to results like this:

![Sampling](images/interpolation_sampling.png)

They can be defined by using:
```java
builder.withSpacingInterpolator(Interpolation.equidistantInterpolation(distance));
```

Interpolator | Description | Example
--- | --- | ---
Natural | The natural interpolator divides each segment (from one BezierVector to another) in the given amount of sub-segments. | ![natural](images/interpolation_natural.png)
Equidistant | The equidistant interpolator sets every point with the provided distance to its neighbour points. | ![equidistant](images/interpolation_equidistant.png)
Angle | The angular interpolator sets points depending on the steepness of the curve. a straight line will therefore only be visible as a start and end point. | ![angular](images/interpolation_angular.png)


### Closing the path

By default, created curves are not closed. You can close the path with
```java
builder.withClosedPath(true);
```
This will connect start and end point of the spline before interpolating anything.
Shape based splines instead will be closed if the Shape method `isPathClosedByDefault()`
is set to true.

### Filters

TODO