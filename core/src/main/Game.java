package main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.game.DataHolderClass;
import com.game.FireBaseInterface;


import java.util.Timer;
import java.util.TimerTask;


import handlers.BoundedCamera;
import handlers.Content;
import handlers.GameStateManager;
import handlers.Input;
import handlers.InputManager;
import states.GameState;
import states.Play;
import states.WinMenu;

/** {@link ApplicationListener} implementation shared by all platforms. */
public class Game implements ApplicationListener {


	public static final String TITLE = "BlockRunner";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;

	FireBaseInterface _FBIC;
	DataHolderClass dataHolder;

	private SpriteBatch sb;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm;

	public static Content res;
	private Play PLay;

	public Game(FireBaseInterface FBIC)
	{
		_FBIC = FBIC;
	}


	public void SetValue(){
		dataHolder = new DataHolderClass();
		//System.out.println("sup");
		_FBIC.SetOnValueChangedListener(dataHolder);
		if(PLay.level==1 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("1", "1");
		}
		if(PLay.level==2 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("2", "1");
		}
		if(PLay.level==3 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("3", "1");
		}
		if(PLay.level==4 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("4", "1");
		}
		if(PLay.level==5 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("5", "1");
		}
		if(PLay.level==6 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("6", "1");
		}
		if(PLay.level==7 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("7", "1");
		}
		if(PLay.level==8 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("8", "1");
		}
		if(PLay.level==9 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("9", "1");
		}
		if(PLay.level==10 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("10", "1");
		}
		if(PLay.level==11 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("11", "1");
		}
		if(PLay.level==12 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("12", "1");
		}
		if(PLay.level==13 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("13", "1");
		}
		if(PLay.level==14 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("14", "1");
		}
		if(PLay.level==15 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("15", "1");
		}
		if(PLay.level==16 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("16", "1");
		}
		if(PLay.level==17 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("17", "1");
		}
		if(PLay.level==18 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("18", "1");
		}
		if(PLay.level==19 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("19", "1");
		}
		if(PLay.level==20 && PLay.winlvl ==1) {
			_FBIC.SetValueInDb("20", "1");
		}
	}


	public void create() {

		Gdx.input.setInputProcessor(new InputManager());

		res = new Content();
		res.loadTexture("res/images/menu.png");
		res.loadTexture("res/images/exit.png");
		res.loadTexture("res/images/bgs.png");
		res.loadTexture("res/images/hud.png");
		res.loadTexture("res/images/back.png");
		res.loadTexture("res/images/player.png");
		res.loadTexture("res/images/cup.png");
		res.loadTexture("res/images/crystal.png");
		res.loadTexture("res/images/cont.png");
		res.loadTexture("res/images/mines.png");

		res.loadSound("res/sfx/jump.wav");
		res.loadSound("res/sfx/crystal.wav");
		res.loadSound("res/sfx/winlevel.wav");
		res.loadSound("res/sfx/hit.wav");
		res.loadSound("res/sfx/changeblock.wav");

		res.loadMusic("res/music/song.ogg");
		res.getMusic("song").setLooping(true);
		res.getMusic("song").setVolume(0.4f);
		res.getMusic("song").play();

		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

		sb = new SpriteBatch();

		gsm = new GameStateManager(this);







	}

	public void render() {

		//Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());



		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		Input.update();

		SetValue();

	}

	public void dispose() {
		res.removeAll();
	}


	public void resize(int w, int h) {}

	public void pause() {}

	public void resume() {}

	public SpriteBatch getSpriteBatch() { return sb; }
	public BoundedCamera getCamera() { return cam; }
	public OrthographicCamera getHUDCamera() { return hudCam; }
	}