# SplineLib

A library for particle path and shape creation. Work on this library is still in progress! Use classes with caution
until release 1.0

TODO erklärendes Bild

## Contents
- [Usage](#usage)
  - [Splines and Terminology](#splines-and-terminology)
  - [Shapes](#shapes)
  - [Registering the Library](#registering-the-library)
  - [Builder](#builder)
    - [Instantiating](#instantiating)
    - [Rounding Interpolator](#rounding-interpolator)
    - [Spacing Interpolator](#spacing-interpolators)
    - [Closing Splines](#closing-the-path)
    - [Filters and Processors](#filters-and-processors)
  - [Creating Phases](#creating-phases)
- [Examples](#examples)

## Usage

### Splines and Terminology

The term "spline" is used to describe smoothing and interpolating functions. In this library, "spline" explicitly means
curves that are defined by any amount of control points. So all those examples are splines:

(white: curve points, red: bezier vectors, green: control points) 

<img src="images/example_circle.png" width="24%"> <img src="images/example_star.png" width="24%"> <img src="images/example_spline.png" width="24%"> <img src="images/example_spline2.png" width="24%">


In context of this library splines are simply lists of BezierVectors. A BezierVector extends the internal Vector class
and contains two further Vectors: leftControlPoint and rightControlPoint. In order to define Bézier curves controlpoints
are obligatory.

### Shapes

Shapes are objects that are defined by a spline. They accept parameters like a pose and a radius. The Pose class is a
combination of a position vector, a direction vector and an up vector. It defines the position and facing direction in
3D space. To get a spline from a shape you can call getSpline() from the Shape interface.

Predefined Shapes can be found in the Shapes class:

```java
public class Example {
	Shapes.rectangle(pose,sizeX,sizeY);
	Shapes.circle(pose,radius);
	Shapes.star(pose,spikes,smoothing,innerRadius,outerRadius);
}
```

### Registering the Library

This library is meant to help with path creation in 3D space. This can for example be useful in Minecraft development.
Bukkits Vector/Location classes are necessary to spawn particles on a curve. The SplineLib class itself is abstract. You
will need to implement methods for converting into an internal vector and back to your required vector class. Here is an
example of doing this for the Minecraft Bukkit Vector class.

```java
public class Example {
  private final SplineLib<org.bukkit.util.Vector> bukkitSplineLib = new SplineLib<>() {
    @Override
    public Vector convertToVector(org.bukkit.util.Vector value) {
      return new Vector(value.getX(), value.getY(), value.getZ());
    }

    @Override
    public org.bukkit.util.Vector convertFromVector(Vector value) {
      return new org.bukkit.util.Vector(value.getX(), value.getY(), value.getZ());
    }

    @Override
    public BezierVector convertToBezierVector(org.bukkit.util.Vector value) {
      return new BezierVector(value.getX(), value.getY(), value.getZ(), null, null);
    }

    @Override
    public org.bukkit.util.Vector convertFromBezierVector(BezierVector value) {
      return new org.bukkit.util.Vector(value.getX(), value.getY(), value.getZ());
    }
  };
}
```

Then you can use the splineLib object to instantiate CurveBuilder objects and to convert Curves to List<
org.bukkit.util.Vector>. You may consider using one SplineLib per world. This will allow you to instantiate/implement
it as SplineLib<org.bukkit.Location> and to convert between Locations and internal Vectors directly.

### Builder

#### Instantiating

The basic way to get a list of vectors from a spline is by using the CurveBuider class. It accepts different parameters
like vectors, splines and shapes (Presets in Shapes class) and can be instantiated by calling

```java
splineLib.newCurveBuilder(Shapes.star(pose,spikes,smoothing,innerRadius,outerRadius))
```

The `.build()` method returns a Curve object. In this case it would simply contain all BezierVectors that were placed in
the constructor. Curves and Splines extends Transformables and can be translated, rotated, scaled and mirrored. You can also
call `.buildAndConvert()`. SplineLib will automatically convert the built curve by using the registered vector
converters. By using the other Builder methods you can define and manipulate the outcome of the build method.

A CurveBuilder is designed to build in two phases. First, it will interpolate the roundness of the spline by using the
defined RoundingInterpolator. It will call the according filtering and processing methods that you have defined in the
builder. Then it will execute the spacing phase and interpolate the spacing of every point of the curve that was created
in the first phase. It will as well call filters and processors.

#### Rounding Interpolator

To interpolate points and achieve smoothing you will need to provide a rounding interpolator. The library contains
several predefined rounding interpolators that can be found in the Interpolation class.

You also can create one by implementing the RoundingInterpolator class.

Define it like so:

```java
builder.withRoundingInterpolator(Interpolation.bezierInterpolation(sampling));
```

The sampling parameter defines how many points the interpolator will create. When using the linear interpolator this may
also be your final result to build. Linear curve interpolation has a natural spacing interpolation by default.

#### Spacing Interpolators

The points that are created by Bezier algorithms are not equidistant by default and vary depending on the steepness of
the curve. To achieve equidistant points you will need to use a spacing interpolator as well. The sampling of the bezier
algorithm defines the smoothness of the curve while the spacing interpolator only moves points along the sampled curve.
Setting a very low sample resolution before using space interpolators will lead to results like this:

![Sampling](images/interpolation_sampling.png)

Spacing interpolators can be defined by calling:
```java
builder.withSpacingInterpolator(Interpolation.equidistantInterpolation(distance));
```

Types | Description | Example
--- | --- | ---
Natural | The natural interpolator divides each segment (from one BezierVector to another) in the given amount of sub-segments. | ![natural](images/interpolation_natural.png)
Equidistant | The equidistant interpolator sets every point with the provided distance to its neighbour points. | ![equidistant](images/interpolation_equidistant.png)
Angle | The angular interpolator sets points depending on the steepness of the curve. a straight line will therefore only be visible as a start and end point. | ![angular](images/interpolation_angular.png)

#### Closing the path

Whether a curve is closed or not is defined by its spline. Shaped splines like those from Circles or Stars are closed by
default. New Splines are not closed by default and have to be closed with

```java
spline.setClosed(true);
```

The curve builder also provides a method in case you never had a spline and instantiated a CurveBuilder with a List of
BezierVectors.

```java
builder.withClosedPath(true);
```

This will connect start and end point of the spline before interpolating anything.

#### Filters and Processors

For each interpolation phase you can provide a point filter. This allows you to filter sample points as well as final
curve points. You can also use the processor to filter based on actual BezierVectors or if you want to rotate/mirror the
curve between interpolations.

```java
builder
        .withSpacingFilter(vector->vector.getY()< 100);
        .withSpacingProcessor(curve->curve.mirror(...));
```

### Creating Phases

TODO

## Examples

### Minecraft Movement Path

In this example I will show how to use this library to visualize a players movement as a smoothed path. Some parts will
be simplyfied but you will get the idea.

First I will create a splinelib for this purpose. To keep things simple I will assume this all happens in the same
world: `world`.

```java
public class PlayerTrack {

  World world; //initialize it somewhere
  SplineLib<Location> bukkitSplineLib = new SplineLib<>() {
    @Override
    public Vector convertToVector(Location value) {
      return new Vector(value.getX(), value.getY(), value.getZ());
    }

    @Override
    public Location convertFromVector(Vector value) {
      return new Location(world, value.getX(), value.getY(), value.getZ());
    }

    @Override
    public BezierVector convertToBezierVector(Location value) {
      org.bukkit.util.Vector dir = value.getDirection().normalize();
      org.bukkit.util.Vector left = value.toVector().subtract(dir);
      org.bukkit.util.Vector right = value.toVector().add(dir);
      return new BezierVector(value.getX(), value.getY(), value.getZ(),
              new Vector(left.getX(), left.getY(), left.getZ()),
              new Vector(right.getX(), right.getY(), right.getZ()));
    }

    @Override
    public Location convertFromBezierVector(BezierVector value) {
      Location location = new Location(world, value.getX(), value.getY(), value.getZ());
      location.setDirection(convertFromVector(value.getRightControlPoint().clone().subtract(value)).toVector());
      return location;
    }
  };
}
```

To record a players movement you can run the following code either depending on the players movement or
in a repeating task:

```java
import org.bukkit.entity.Player;

public class PlayerTrack {
  private final Spline spline = bukkitSplineLib.newSpline();

  //Call under certain conditions. The converter sets the control points
  //with a distance of 1. Create new points on the spline
  //with a distance of around 3-5 blocks to achieve smooth paths.
  //or change the control point distance in the converter.
  public void recordLocation(Player player) {

    //convert location to bezier vector
    //it uses eyelocation because we track the facing direction by converting.
    //The result will represent the players view like a camera path.
    spline.add(bukkitSplineLib.convertToBezierVector(player.getEyeLocation()));
  }
}
```
Now you can display the Spline by converting it into a List of Locations:

```java
public class PlayerTrack {
  private final Spline spline = bukkitSplineLib.newSpline();

  public void displaySpline(Player player) {

    //don't call this many times if the spline does not change - cache the result 
    //instead if you want to respawn the particles with a repeating task
    List<Location> locations = bukkitSplineLib.newSplineBuilder(spline)
            //using 15 samples per ~3 blocks
            .withRoundingInterpolation(Interpolation.bezierInterpolation(15))
            //setting distance of final locations to 0.3 blocks
            .withSpacingInterpolation(Interpolation.equidistantInterpolation(0.3))
            //convert to List<Location> directly
            .buildAndConvert();

    //do this as a repeating task or cache the locations List if you want to summon the
    //spline particles only at some certain conditions.
    for (Location location : locations) {
      player.spawnParticle(Particle.FLAME, location, 1);
    }
  }
}
```

If you don't want to interpolate a spline every view ticks but still want to respawn particles at the players location
you may want to build it as Curve object (`.build()`) and move it by using the `translate(vectorToNewLocation)`. Then
you can convert the Curve by using `List<Location> locations = bukkitSplineLib.convert(curve);`



### Minecraft Heart Above Players

In this example, a heart will appear above a player and will follow him.
We will therefore use the bukkitSplineLib of the example above.

What we want to achieve:
- If a player joins, a heart will be placed above his head.
- It will follow the player for 20 seconds and then disappears.
- It will be updated twice per second.
- The heart will adjust its direction based on the players pitch.

We will need a spline lib and a listener. Also, we need a repeating task to display
the particles twice per second.
````java
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Hearts implements Listener {
  SplineLib<Location> splineLib = ...;
  private int taskId = 0;
  
  public Hearts() {
  	startTimer();
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    //create Curve and store it    
  }
  
  public void startTimer() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
        //display particles for all players;
    }, 0, 10);
  }
}
````

Now we want to create a Heart and store it in a Map:

````java
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Hearts implements Listener {

  Map<Player, Curve> playerHeartMap = new HashMap<>();

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    //create a Pose for the heart to occur (3 blocks above player)
    Pose pose = splineLib.newPose(player.getLocation(), player.getLocation().getDirection());

    //create the heart curve
    Curve curve = splineLib.newSplineBuilder(Shapes.heart(pose, /*size*/ 2, /*smoothness*/ 0.5))
            .withRoundingInterpolation(Interpolation.bezierInterpolation(10))
            .withSpacingInterpolation(Interpolation.equidistantInterpolation(0.2))
            .build();

    //store the curve into
    playerHeartMap.put(player, curve);
  }
}
````