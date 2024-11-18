package com.myprojects.pacman;

import com.myprojects.pacman.api.Game;
import com.myprojects.pacman.impl.Pacman;

public class Start {

	public static void main(final String[] args) {
		final Game pacman = new Pacman();
		pacman.run();
	}
}
