package com.myprojects.impl;

import com.myprojects.api.Vector;

public class Vector2 implements Vector {

    public double x;
    public double y;

    public Vector2(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public Vector2 add(final Vector2 v) {
        return Vector2.add(this, v);
    }

    public Vector2 sub(final Vector2 v) {
        return Vector2.sub(this, v);
    }

    public Vector2 invert() {
        return Vector2.invert(this);
    }

    public static Vector2 add(final Vector2 v1, final Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2 sub(final Vector2 v1, final Vector2 v2) {
        return add(v1, v2.invert());
    }

    public static Vector2 invert(final Vector2 v) {
        return new Vector2(-v.x, -v.y);
    }

    public static Vector2 zero() {
        return new Vector2();
    }
}
