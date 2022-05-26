package states;


import static handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import main.Game;

import entities.Crystal;
import entities.HUD;
import entities.Mines;
import entities.Player;
import handlers.B2DVars;
import handlers.Background;
import handlers.BoundedCamera;
import handlers.CollisionManager;
import handlers.GameStateManager;
import handlers.Input;


public class Play extends GameState {
	
	private boolean debug = false;
	
	private World world;
	private Box2DDebugRenderer b2dRenderer;
	private CollisionManager cl;
	private BoundedCamera b2dCam;
	
	private Player player;
	
	private TiledMap tileMap;
	private int tileMapWidth;
	private int tileMapHeight;
	private int tileSize;
	private OrthogonalTiledMapRenderer tmRenderer;
	
	private Array<Crystal> crystals;
	private Array<Mines> mines;
	
	private Background[] backgrounds;
	private HUD hud;

	public static int level;
	public static int winlvl = 0;

	public Play(GameStateManager gsm) {
		
		super(gsm);

		world = new World(new Vector2(0, -7f), true);
		cl = new CollisionManager();
		world.setContactListener(cl);
		b2dRenderer = new Box2DDebugRenderer();
		createPlayer();
		createWalls();
		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
		createCrystals();
		player.setTotalCrystals(crystals.size);
		createMines();
		
		// фон
		Texture bgs = Game.res.getTexture("bgs");
		TextureRegion sky = new TextureRegion(bgs, 0, 0, 320, 240);
		TextureRegion clouds = new TextureRegion(bgs, 0, 240, 320, 240);
		TextureRegion mountains = new TextureRegion(bgs, 0, 480, 320, 240);
		backgrounds = new Background[3];
		backgrounds[0] = new Background(sky, cam, 0f);
		backgrounds[1] = new Background(clouds, cam, 0.1f);
		backgrounds[2] = new Background(mountains, cam, 0.2f);

		hud = new HUD(player);

		b2dCam = new BoundedCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);
		b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);
		
	}



	/**
	 * Создание игрока.
	 */
	private void createPlayer() {

		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(60 / PPM, 120 / PPM);
		bdef.fixedRotation = true;
		bdef.linearVelocity.set(1f, 0f);

		Body body = world.createBody(bdef);
		
		// создание формы для коллизии игрока
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(13 / PPM, 13 / PPM);

		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1;
		fdef.friction = 0;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_CRYSTAL | B2DVars.BIT_MINE;

		body.createFixture(fdef);
		shape.dispose();
		
		// создание формы для коллизии ног
		shape = new PolygonShape();
		shape.setAsBox(13 / PPM, 3 / PPM, new Vector2(0, -13 / PPM), 0);

		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fdef.filter.maskBits = B2DVars.BIT_RED;

		body.createFixture(fdef).setUserData("foot");;
		shape.dispose();

		player = new Player(body);
		body.setUserData(player);

		MassData md = body.getMassData();
		md.mass = 1;
		body.setMassData(md);

		
	}
	
	/**
	 * Карта
	 */
	private void createWalls() {
		
		// загрузка карты
		try {
			tileMap = new TmxMapLoader().load("res/maps/level" + level + ".tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: res/maps/level" + level + ".tmx");
			Gdx.app.exit();
		}
		tileMapWidth = (int) tileMap.getProperties().get("width");
		tileMapHeight = (int) tileMap.getProperties().get("height");
		tileSize = (int) tileMap.getProperties().get("tilewidth");
		tmRenderer = new OrthogonalTiledMapRenderer(tileMap);
		
		// считать каждый из слоёв "red" "green" и "blue"
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		createBlocks(layer, B2DVars.BIT_RED);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
		createBlocks(layer, B2DVars.BIT_GREEN);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
		createBlocks(layer, B2DVars.BIT_BLUE);

	}
	
	/**
	 * Создание box2d тел
	 */
	private void createBlocks(TiledMapTileLayer layer, short bits) {

		float ts = layer.getTileWidth();

		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {

				Cell cell = layer.getCell(col, row);

				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				BodyDef bdef = new BodyDef();
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
				v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
				v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
				cs.createChain(v);
				FixtureDef fd = new FixtureDef();
				fd.friction = 0;
				fd.shape = cs;
				fd.filter.categoryBits = bits;
				fd.filter.maskBits = B2DVars.BIT_PLAYER;
				world.createBody(bdef).createFixture(fd);
				cs.dispose();
				
			}
		}
		
	}
	
	/**
	 * box2d тела для кристаллов со слоя "crystals"
	 */
	private void createCrystals() {
		crystals = new Array<Crystal>();
		MapLayer ml = tileMap.getLayers().get("crystals");
		if(ml == null) return;
		
		for(MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			float x = (float) mo.getProperties().get("x") / PPM;
			float y = (float) mo.getProperties().get("y") / PPM;
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();
			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / PPM);
			cfdef.shape = cshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("crystal");
			Crystal c = new Crystal(body);
			body.setUserData(c);
			crystals.add(c);
			cshape.dispose();
		}
	}
	
	/**
	 * box2d тела для мин со слоя "мины"
	 */
	private void createMines() {
		
		mines = new Array<Mines>();
		
		MapLayer ml = tileMap.getLayers().get("spikes");
		if(ml == null) return;
		
		for(MapObject mo : ml.getObjects()) {
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			float x = (float) mo.getProperties().get("x") / PPM;
			float y = (float) mo.getProperties().get("y") / PPM;
			cdef.position.set(x, y);
			Body body = world.createBody(cdef);
			FixtureDef cfdef = new FixtureDef();
			CircleShape cshape = new CircleShape();
			cshape.setRadius(5 / PPM);
			cfdef.shape = cshape;
			cfdef.isSensor = true;
			cfdef.filter.categoryBits = B2DVars.BIT_MINE;
			cfdef.filter.maskBits = B2DVars.BIT_PLAYER;
			body.createFixture(cfdef).setUserData("mine");
			Mines s = new Mines(body);
			body.setUserData(s);
			mines.add(s);
			cshape.dispose();
		}
		
	}

	private void playerJump() {
		if(cl.playerCanJump()) {
			player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
			player.getBody().applyForceToCenter(0, 200, true);
			Game.res.getSound("jump").play();
		}
	}
	
	/**
	 * Смена бит-маски для игрока
	 */
	private void switchBlocks() {
		
		// считать маску ног
		Filter filter = player.getBody().getFixtureList().get(1).getFilterData();
		short bits = filter.maskBits;

		// красный -> зелёный -> синий

		if((bits &  B2DVars.BIT_RED) != 0){
			bits &= ~B2DVars.BIT_RED;
			bits |= B2DVars.BIT_GREEN;
		}
		else if((bits & B2DVars.BIT_GREEN) != 0){
			bits &= ~B2DVars.BIT_GREEN;
			bits |= B2DVars.BIT_BLUE;
		}
		else if((bits & B2DVars.BIT_BLUE) != 0){
			bits &= ~B2DVars.BIT_BLUE;
			bits |= B2DVars.BIT_RED;
		}
		
		// бит маска для ног
		filter.maskBits = bits;
		player.getBody().getFixtureList().get(1).setFilterData(filter);

		// бит маска игрока
		bits |= B2DVars.BIT_CRYSTAL | B2DVars.BIT_MINE;
		filter.maskBits = bits;
		player.getBody().getFixtureList().get(0).setFilterData(filter);
		Game.res.getSound("changeblock").play();
		
	}
	
	public void handleInput() {
		if(Input.isPressed(Input.BUTTON1)) {
			playerJump();
		}
		if(Input.isPressed(Input.BUTTON2)) {
			switchBlocks();
		}
		if(Input.isPressed()) {
			if(Input.x < Gdx.graphics.getWidth() / 2) {
				switchBlocks();
			}
			else {
				playerJump();
			}
		}
		
	}
	
	public void update(float dt) {
		handleInput();
		world.step(Game.STEP, 1, 1);
		
		// сбор кристаллов
		Array<Body> bodies = cl.getBodies();
		for(int i = 0; i < bodies.size; i++) {
			Body b = bodies.get(i);
			crystals.removeValue((Crystal) b.getUserData(), true);
			world.destroyBody(bodies.get(i));
			player.collectCrystal();
			Game.res.getSound("crystal").play();
		}
		bodies.clear();

		player.update(dt);
		
		// победа
		if(player.getBody().getPosition().x * PPM > tileMapWidth * tileSize) {
			Game.res.getSound("winlevel").play();



			if (level == 1){
				gsm.setState(GameStateManager.WINMENU);
				winlvl = 1;
			}
			if (level == 2){

				gsm.setState(GameStateManager.WINMENU);
				winlvl = 1;
			}
			if (level == 3){

				gsm.setState(GameStateManager.WINMENU);
				winlvl = 1;
			}
			if (level == 4){

				gsm.setState(GameStateManager.WINMENU);
				winlvl = 1;
			}
			if (level == 5){
				gsm.setState(GameStateManager.WINMENU);
				winlvl = 1;

			}if (level == 6){
				gsm.setState(GameStateManager.WINMENU);
				winlvl = 1;
			}
			if (level == 7){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 8){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 9){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 10){
				gsm.setState(GameStateManager.WINMENU);

			}if (level == 11){
				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 12){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 13){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 14){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 15){
				gsm.setState(GameStateManager.WINMENU);

			}if (level == 16){
				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 17){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 18){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 19){

				gsm.setState(GameStateManager.WINMENU);
			}
			if (level == 20){
				gsm.setState(GameStateManager.WINMENU);

			}
		}
		
		// поражение
		if(player.getBody().getPosition().y < 0) {
			Game.res.getSound("hit").play();
			gsm.setState(GameStateManager.MENU);
		}
		if(player.getBody().getLinearVelocity().x < 0.001f) {
			Game.res.getSound("hit").play();
			gsm.setState(GameStateManager.MENU);
		}
		if(cl.isPlayerDead()) {
			Game.res.getSound("hit").play();
			gsm.setState(GameStateManager.MENU);
		}

		for(int i = 0; i < crystals.size; i++) {
			crystals.get(i).update(dt);
		}

		for(int i = 0; i < mines.size; i++) {
			mines.get(i).update(dt);
		}
		
	}
	
	public void render() {
		
		// камера за игроком
		cam.setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2);
		cam.update();
		
		// фон
		sb.setProjectionMatrix(hudCam.combined);
		for(int i = 0; i < backgrounds.length; i++) {
			backgrounds[i].render(sb);
		}
		
		// карта
		tmRenderer.setView(cam);
		tmRenderer.render();
		
		// игрок
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		
		// кристаллы
		for(int i = 0; i < crystals.size; i++) {
			crystals.get(i).render(sb);
		}
		
		// мины
		for(int i = 0; i < mines.size; i++) {
			mines.get(i).render(sb);
		}
		
		// худ
		sb.setProjectionMatrix(hudCam.combined);
		hud.render(sb);

		if(debug) {
			b2dCam.setPosition(player.getPosition().x + Game.V_WIDTH / 4 / PPM, Game.V_HEIGHT / 2 / PPM);
			b2dCam.update();
			b2dRenderer.render(world, b2dCam.combined);
		}
		
	}
	
	public void dispose() {
	}
	
}