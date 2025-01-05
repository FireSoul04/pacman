package com.firesoul.pacman.impl.util;

import java.io.Serializable;

public record Pair<X, Y>(X x, Y y) implements Serializable {
}
