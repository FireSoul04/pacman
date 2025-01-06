package com.firesoul.editor.gui;

import java.io.Serializable;

public record Pair<X, Y>(X x, Y y) implements Serializable {
}
