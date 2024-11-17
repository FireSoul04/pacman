package com.myprojects.api;

import com.myprojects.impl.Vector2;

public interface Vector {

    Vector2 add(final Vector2 v);

    Vector2 sub(final Vector2 v);

    Vector2 invert();
}
