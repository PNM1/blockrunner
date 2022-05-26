package states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.DataHolderClass;

import main.Game;

import handlers.BackButton;
import handlers.Buttons;
import handlers.GameStateManager;

public class LevelSelect extends GameState {
	
	private TextureRegion reg;

	DataHolderClass dataHolderClass;



	private Buttons[][] buttons;

	private BackButton backButton;

	
	public LevelSelect(GameStateManager gsm) {
		
		super(gsm);

		
		reg = new TextureRegion(Game.res.getTexture("bgs"), 0, 0, 320, 240);
		
		TextureRegion buttonReg = new TextureRegion(Game.res.getTexture("hud"), 0, 0, 32, 32);
		buttons = new Buttons[4][5];
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col] = new Buttons(buttonReg, 80 + col * 40, 190 - row * 40, cam);
				buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
			}
		}
		Texture tex = Game.res.getTexture("back");
		backButton = new BackButton(new TextureRegion(tex), 160, 25, cam);
		
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		
	}
	
	public void handleInput() {
		if(backButton.isClicked()){
			Game.res.getSound("crystal").play();
			gsm.setState(GameStateManager.MENU);
		}
	}
	
	public void update(float dt) {
		
		handleInput();
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].update(dt);
				if(buttons[row][col].isClicked()) {
					Play.level = row * buttons[0].length + col + 1;
					Game.res.getSound("crystal").play();
					gsm.setState(GameStateManager.PLAY);
				}
			}
		}
		stars();
		
	}
	public void stars(){
		/*String a5 = dataHolderClass.someValue5;
		if(a5.equals("1")){

			Texture tex = Game.res.getTexture("back");
			backButton = new BackButton(new TextureRegion(tex), 1,1, cam);
		}

		 */

	}
	
	public void render() {
		
		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		sb.draw(reg, 0, 0);
		sb.end();
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].render(sb);
			}
		}

		backButton.render(sb);
		
	}
	
	public void dispose() {

	}
	
}
