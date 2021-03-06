package handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

/**
 * Simple image button.
 */
public class BackButton {

	// x, y
	private float x;
	private float y;
	private float width;
	private float height;

	private TextureRegion reg;

	Vector3 vec;
	private OrthographicCamera cam;

	private boolean clicked;


	public BackButton(TextureRegion reg, float x, float y, OrthographicCamera cam) {
		
		this.reg = reg;
		this.x = x;
		this.y = y;
		this.cam = cam;
		
		width = reg.getRegionWidth();
		height = reg.getRegionHeight();
		vec = new Vector3();
		

	}
	
	public boolean isClicked() { return clicked; }

	
	public void update(float dt) {
		
		vec.set(Input.x, Input.y, 0);
		cam.unproject(vec);
		
		if(Input.isPressed() &&
			vec.x > x - width / 2 && vec.x < x + width / 2 &&
			vec.y > y - height / 2 && vec.y < y + height / 2) {
			clicked = true;
		}
		else {
			clicked = false;
		}
		
	}
	
	public void render(SpriteBatch sb) {
		
		sb.begin();
		
		sb.draw(reg, x - width / 2, y - height / 2);
		
		sb.end();
		
	}
	

	
}
