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

This is [on GitHub](https://github.com/jbt/markdown-editor) so let me know if I've b0rked it somewhere.


Props to Mr. Doob and his [code editor](http://mrdoob.com/projects/code-editor/), from which
the inspiration to this, and some handy implementation hints, came.

### Stuff used to make this:

 * [markdown-it](https://github.com/markdown-it/markdown-it) for Markdown parsing
 * [CodeMirror](http://codemirror.net/) for the awesome syntax-highlighted editor
 * [highlight.js](http://softwaremaniacs.org/soft/highlight/en/) for syntax highlighting in output code blocks
 * [js-deflate](https://github.com/dankogai/js-deflate) for gzipping of data to make it fit in URLs
