package com.firesoul.pacman;

import com.firesoul.pacman.api.controller.Game;
import com.firesoul.pacman.impl.controller.Pacman;
import com.firesoul.pacman.impl.model.Map2D;

public class Start {

	public static void main(final String[] args) {
		//Map2D.createMap();
		final Game pacman = new Pacman();
		pacman.init();
	}
}
