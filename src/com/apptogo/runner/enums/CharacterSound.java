package com.apptogo.runner.enums;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.physics.box2d.World;

/** przy dodawaniu kolejnego typu pamietac o obsluzeniu go w funkcji createCharacter klasy gameWorld
 *  oraz w tablicy z selectCharacter w MainMenuScreen                                              */
public enum CharacterSound
{
	BANDIT_JUMP, STEPS
}