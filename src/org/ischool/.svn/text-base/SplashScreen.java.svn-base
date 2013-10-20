package org.ischool;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.entity.shape.modifier.DelayModifier;
import org.anddev.andengine.entity.shape.modifier.FadeOutModifier;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier.IShapeModifierListener;
import org.anddev.andengine.entity.shape.modifier.SequenceShapeModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.IModifier;

import android.content.Context;
import android.content.Intent;

public class SplashScreen extends BaseGameActivity
{
	private static int CAMERA_WIDTH = 480;
	private static int CAMERA_HEIGHT = 763;

	private Camera mCamera;

	Context context;

	private Texture logoTexture;
	private TextureRegion logoTextureRegion;

	@Override
	public Engine onLoadEngine()
	{
		this.context = getApplicationContext();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		Engine Engine = new Engine(new EngineOptions(false, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
		return Engine;
	}

	@Override
	public void onLoadResources()
	{
		logoTexture = new Texture(512, 1024);
		TextureRegionFactory.setAssetBasePath("gfx/");
		logoTextureRegion = TextureRegionFactory.createFromAsset(logoTexture, context, "splash_screen.jpg", 0, 0);
		mEngine.getTextureManager().loadTexture(logoTexture);
	}

	@Override
	public Scene onLoadScene()
	{
		Scene mScene = new Scene(1);
		mScene.setBackground(new ColorBackground(1, 1, 1, 0.5f));

		//定义精灵并将精灵初始化为透明
		final int x = (CAMERA_WIDTH - this.logoTextureRegion.getWidth()) / 2;
		final int y = (CAMERA_HEIGHT - this.logoTextureRegion.getHeight()) / 2;

		Sprite sprite = new Sprite(x, y, logoTextureRegion);
		sprite.setAlpha(0.0f);
		mScene.getTopLayer().addEntity(sprite);

		sprite.addShapeModifier(new SequenceShapeModifier(new IShapeModifierListener()
		{

			@Override
			public void onModifierFinished(IModifier<IShape> pModifier, IShape pItem)
			{
				//进入主菜单界面
				Intent intent = new Intent(context, LogIn.class);
				SplashScreen.this.startActivity(intent);
				SplashScreen.this.finish();
			}
		}, 
		new AlphaModifier(3.0f, 0.0f, 1.0f), 
		new DelayModifier(3.0f),
		new FadeOutModifier(1.0f)));

		return mScene;
	}

	@Override
	public void onLoadComplete()
	{
	}

}