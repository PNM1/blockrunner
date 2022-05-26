package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import main.Game;

public class Mines extends entities.B2DSprite {
	
	public Mines(Body body) {
		
		super(body);
		
		Texture tex = Game.res.getTexture("mines");
		TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
		animation.setFrames(sprites, 1 / 12f);
		
		width = sprites[0].getRegionWidth();
		height = sprites[0].getRegionHeight();
		
	}
	
}
