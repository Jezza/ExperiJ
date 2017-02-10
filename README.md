# ExperiJ

ExperiJ is a fast and customisable runtime experiment manager for Java.

Features include:

 * Ease of use.
 	* Annotation based.
 * Experiment results accessible during runtime.

Note: It doesn't run the experiments in parallel, but one after the other, with the control being first.

##
### Usage

```java
@Control
public static int controlMethod(String originalPath) {
	// Some random code
}

@Experiment
public static String experiment1() {
	// Some random code
}

@Experiment
public static String experiment2() {
	// Some random code
}

@Experiment
public static String experiment3() {
	// Some random code
}
```
