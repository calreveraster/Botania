/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 13, 2014, 9:01:32 PM (GMT)]
 */

package vazkii.botania.common.core.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibPotionNames;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class ConfigHandler 
{

	//attempt to set up some config categories
	public static class Categories 
	{
		public static final String CATEGORY_SHADERS = "Shaders";
	}

	//private static String directory;
	public static Configuration config;
	public static ConfigAdaptor adaptor;

	private static final String CATEGORY_POTIONS = "potions";

	public static int hardcorePassiveGeneration = 72000;

	public static boolean useAdaptativeConfig = true;

	//Going to split up the shader config per-shader.
	public static boolean useShaders = true;

	public static boolean usePylonGlowShader = true;
	public static boolean useEnchanterRuneShader = true;
	public static boolean useManaPoolShader = true;
	public static boolean useDopplegangerShader = true;
	public static boolean useHaloShader = true;
	public static boolean useDopplegangerBarShader = true;
	public static boolean useTerraplateRuneShader = true;
	public static boolean useFilmGrainShader = true;
	public static boolean useGoldShader = true;
	public static boolean useCategoryButtonShader = true;

	public static boolean lexiconRotatingItems = true;
	public static boolean lexiconJustifiedText = false;
	public static boolean subtlePowerSystem = false;
	public static boolean staticWandBeam = false;
	public static boolean boundBlockWireframe = true;
	public static boolean lexicon3dModel = true;
	public static boolean oldPylonModel = false;
	public static double flowerParticleFrequency = 0.75F;
	public static boolean blockBreakParticles = true;
	public static boolean blockBreakParticlesTool = true;
	public static boolean elfPortalParticlesEnabled = true;
	public static boolean chargingAnimationEnabled = true;
	public static boolean useVanillaParticleLimiter = true;
	public static boolean silentSpreaders = false;
	public static boolean renderBaubles = true;
	public static boolean enableSeasonalFeatures = true;
	public static boolean useShiftForQuickLookup = false;
	public static boolean enableArmorModels = true;
	public static boolean enableFancySkybox = true;
	public static boolean enableFancySkyboxInNormalWorlds = false;
	
	public static int manaBarHeight = 29;
	public static int flightBarHeight = 49;
	public static int flightBarBreathHeight = 59;
	public static int glSecondaryTextureUnit = 7;

	public static boolean altFlowerTextures = false;
	public static boolean matrixMode = false;
	public static boolean referencesEnabled = true;

	public static boolean versionCheckEnabled = true;
	public static int spreaderPositionShift = 1;
	public static boolean flowerForceCheck = true;
	public static boolean enderPickpocketEnabled = true;

	public static boolean fallenKanadeEnabled = true;
	public static boolean darkQuartzEnabled = true;
	public static boolean enchanterEnabled = true;
	public static boolean fluxfieldEnabled = true;
	public static boolean relicsEnabled = true;
	public static boolean stones18Enabled = true;
	public static boolean ringOfOdinFireResist = true;
	public static boolean enderStuff19Enabled = true;
	public static boolean invertMagnetRing = false;
	public static boolean enableThaumcraftStablizers = true;
	
	public static int harvestLevelWeight = 2;
	public static int harvestLevelBore = 3;

	public static int flowerQuantity = 2;
	public static int flowerDensity = 2;
	public static int flowerPatchSize = 6;
	public static int flowerPatchChance = 16;
	public static double flowerTallChance = 0.05;
	public static int mushroomQuantity = 40;

	private static boolean verifiedPotionArray = false;
	private static int potionArrayLimit = 0;

	public static int potionIDSoulCross = 91;
	public static int potionIDFeatherfeet = 92;
	public static int potionIDEmptiness = 93;
	public static int potionIDBloodthirst = 94;
	public static int potionIDAllure = 95;
	public static int potionIDClear = 96;
	
	
	public static void loadConfig(File configFile) {
		config = new Configuration(configFile);

		config.load();
		load();

		FMLCommonHandler.instance().bus().register(new ChangeListener());
	}

	public static void load() {
		String desc;

		desc = "Set this to false to disable the Adaptative Config. Adaptative Config changes any default config values from old versions to the new defaults to make sure you aren't missing out on changes because of old configs. It will not touch any values that were changed manually.";
		useAdaptativeConfig = loadPropBool("adaptativeConfig.enabled", desc, useAdaptativeConfig);
		adaptor = new ConfigAdaptor(useAdaptativeConfig);

		//original shader set 
		//Ill probably leave this as a master disable for now to avoid changing some other logic.
		desc = "Set this to false to disable the use of shaders for some of the mod's renders.";
		useShaders = loadPropBool("shaders.enabled", desc, useShaders);

		/*
		* New Shader sets
		* These will replace the above option to individually disable certain shaders.
		*/

		desc = "Set this to false to disable the Pylon Glow Shader";
		usePylonGlowShader = loadPropBool("pylonGlowShaders.enabled", desc, usePylonGlowShader);

		desc = "Set this to false to disable the Enchanter Rune Shader";
		useEnchanterRuneShader = loadPropBool("enchanterRuneShaders.enabled", desc, useEnchanterRuneShader);

		desc = "Set this to false to disable the Mana Pool Shader";
		useManaPoolShader = loadPropBool("manaPoolShaders.enabled", desc, useManaPoolShader);

		desc = "Set this to false to disable the Doppleganger Shader";
		useDopplegangerShader = loadPropBool("dopplegangerShaders.enabled", desc, useDopplegangerShader);

		desc = "Set this to false to disable the Halo Shader";
		useHaloShader = loadPropBool("haloShaders.enabled", desc, useHaloShader);

		desc = "Set this to false to disable the Doppleganger Bar Shader";
		useDopplegangerBarShader = loadPropBool("dopplegangerBarShaders.enabled", desc, useDopplegangerBarShader);

		desc = "Set this to false to disable the Terraplate Rune Shader";
		useTerraplateRuneShader = loadPropBool("terraplateRuneShaders.enabled", desc, useTerraplateRuneShader);

		desc = "Set this to false to disable the Film Grain Shader";
		useFilmGrainShader = loadPropBool("filmGrainShaders.enabled", desc, useFilmGrainShader);

		desc = "Set this to false to disable the Gold Shader";
		useGoldShader = loadPropBool("goldShaders.enabled", desc, useGoldShader);

		desc = "Set this to false to disable the Film Grain Shader";
		useCategoryButtonShader = loadPropBool("categoryButtonShaders.enabled", desc, useCategoryButtonShader);

		/*
		* End new Shader Configs
		* You are now returned to your regularly scheduled config-ing
		*/

		desc = "Set this to false to disable the rotating items in the petal and rune entries in the Lexica Botania.";
		lexiconRotatingItems = loadPropBool("lexicon.enable.rotatingItems", desc, lexiconRotatingItems);

		desc = "Set this to true to enable justified text in the Lexica Botania's text pages.";
		lexiconJustifiedText = loadPropBool("lexicon.enable.justifiedText", desc, lexiconJustifiedText);

		desc = "Set this to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.";
		subtlePowerSystem = loadPropBool("powerSystem.subtle", desc, subtlePowerSystem);

		desc = "Set this to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old Botania versions. Warning: Disabled by default because it may be laggy.";
		staticWandBeam = loadPropBool("wandBeam.static", desc, staticWandBeam);

		desc = "Set this to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).";
		boundBlockWireframe = loadPropBool("boundBlock.wireframe.enabled", desc, boundBlockWireframe);

		desc = "Set this to false to disable the animated 3D render for the Lexica Botania.";
		lexicon3dModel = loadPropBool("lexicon.render.3D", desc, lexicon3dModel);

		desc = "Set this to true to use the old (non-.obj, pre beta18) pylon model";
		oldPylonModel = loadPropBool("pylonModel.old", desc, oldPylonModel);

		desc = "The frequency in which particles spawn from normal (worldgen) mystical flowers";
		flowerParticleFrequency = loadPropDouble("flowerParticles.frequency", desc, flowerParticleFrequency);

		desc = "Set this to false to remove the block breaking particles from the flowers and other items in the mod.";
		blockBreakParticles = loadPropBool("blockBreakingParticles.enabled", desc, blockBreakParticles);

		desc = "Set this to false to remove the block breaking particles from the Mana Shatterer, as there can be a good amount in higher levels.";
		blockBreakParticlesTool = loadPropBool("blockBreakingParticlesTool.enabled", desc, blockBreakParticlesTool);

		desc = "Set this to false to disable the particles in the elven portal.";
		elfPortalParticlesEnabled = loadPropBool("elfPortal.particles.enabled", desc, elfPortalParticlesEnabled);

		desc = "Set this to false to disable the animation when an item is charging on top of a mana pool.";
		chargingAnimationEnabled = loadPropBool("chargeAnimation.enabled", desc, chargingAnimationEnabled);

		desc = "Set this to false to always display all particles regardless of the \"Particles\" setting in the Vanilla options menu.";
		useVanillaParticleLimiter = loadPropBool("vanillaParticleConfig.enabled", desc, useVanillaParticleLimiter);

		desc = "Set this to true to disable the mana spreader shooting sound.";
		silentSpreaders = loadPropBool("manaSpreaders.silent", desc, silentSpreaders);

		desc = "Set this to false to disable rendering of baubles in the player.";
		renderBaubles = loadPropBool("baubleRender.enabled", desc, renderBaubles);

		desc = "Set this to false to disable seasonal features, such as halloween and christmas.";
		enableSeasonalFeatures = loadPropBool("seasonalFeatures.enabled", desc, enableSeasonalFeatures);

		desc = "Set this to true to use Shift instead of Ctrl for the inventory lexica botania quick lookup feature.";
		useShiftForQuickLookup = loadPropBool("quickLookup.useShift", desc, useShiftForQuickLookup);

		desc = "Set this to false to disable custom armor models.";
		enableArmorModels = loadPropBool("armorModels.enable", desc, enableArmorModels);

		desc = "Set this to false to disable the fancy skybox in Garden of Glass.";
		enableFancySkybox = loadPropBool("fancySkybox.enable", desc, enableFancySkybox);
		
		desc = "Set this to true to enable the fancy skybox in non Garden of Glass worlds. (Does not require Garden of Glass loaded to use, needs 'fancySkybox.enable' to be true as well)";
		enableFancySkyboxInNormalWorlds = loadPropBool("fancySkybox.normalWorlds", desc, enableFancySkyboxInNormalWorlds);
		
		desc = "The height of the mana display bar in above the XP bar. You can change this if you have a mod that changes where the XP bar is.";
		manaBarHeight = loadPropInt("manaBar.height", desc, manaBarHeight);

		desc = "The height of the Flugel Tiara flight bar. You can change this if you have a mod that adds a bar in that spot.";
		flightBarHeight = loadPropInt("flightBar.height", desc, flightBarHeight);

		desc = "The height of the Flugel Tiara flight bar if your breath bar is shown. You can change this if you have a mod that adds a bar in that spot.";
		flightBarBreathHeight = loadPropInt("flightBarBreath.height", desc, flightBarBreathHeight);
		
		desc = "The GL Texture Unit to use for the secondary sampler passed in to the Lexica Botania's category button shader. DO NOT TOUCH THIS IF YOU DON'T KNOW WHAT YOU'RE DOING";
		glSecondaryTextureUnit = loadPropInt("shaders.secondaryUnit", desc, glSecondaryTextureUnit);

		desc = "Set this to true to use alternate flower textures by Futureazoo, not all flowers are textured. http://redd.it/2b3o3f";
		altFlowerTextures = loadPropBool("flowerTextures.alt", desc, altFlowerTextures);

		desc = "Set this to true if you are the chosen one. For lovers of glitch art and just general mad people.";
		matrixMode = loadPropBool("matrixMode.enabled", desc, matrixMode);

		desc = "Set this to false to disable the references in the flower tooltips. (You monster D:)";
		referencesEnabled = loadPropBool("references.enabled", desc, referencesEnabled);

		desc = "Set this to false to disable checking and alerting when new Botania versions come out. (keywords for noobs: update notification message)";
		versionCheckEnabled = loadPropBool("versionChecking.enabled", desc, versionCheckEnabled);

		desc = "Do not ever touch this value if not asked to. Possible symptoms of doing so include your head turning backwards, the appearance of Titans near the walls or you being trapped in a game of Sword Art Online.";
		spreaderPositionShift = loadPropInt("spreader.posShift", desc, spreaderPositionShift);

		desc = "Turn this off ONLY IF you're on an extremely large world with an exaggerated count of Mana Spreaders/Mana Pools and are experiencing TPS lag. This toggles whether flowers are strict with their checking for connecting to pools/spreaders or just check whenever possible.";
		flowerForceCheck = loadPropBool("flower.forceCheck", desc, flowerForceCheck);

		desc = "Set to false to disable the ability for the Hand of Ender to pickpocket other players' ender chests.";
		enderPickpocketEnabled = loadPropBool("enderPickpocket.enabled", desc, enderPickpocketEnabled);

		desc = "Set this to false to disable the Fallen Kanade flower (gives Regeneration). This config option is here for those using Blood Magic. Note: Turning this off will not remove ones already in the world, it'll simply prevent the crafting.";
		fallenKanadeEnabled = loadPropBool("fallenKanade.enabled", desc, fallenKanadeEnabled);

		desc = "Set this to false to disable the Smokey Quartz blocks. This config option is here for those using Thaumic Tinkerer";
		darkQuartzEnabled = loadPropBool("darkQuartz.enabled", desc, darkQuartzEnabled);

		desc = "Set this to false to disable the Mana Enchanter. Since some people find it OP or something. This only disables the entry and creation. Old ones that are already in the world will stay.";
		enchanterEnabled = loadPropBool("manaEnchanter.enabled", desc, enchanterEnabled);

		desc = "Set this to false to disable the Mana Fluxfield (generates RF from mana). This only disables the entry and creation. Old ones that are already in the world will stay.";
		fluxfieldEnabled = loadPropBool("manaFluxfield.enabled", desc, fluxfieldEnabled);

		desc = "Set this to false to disable the Relic System. This only disables the entries, drops and achievements. Old ones that are already in the world will stay.";
		relicsEnabled = loadPropBool("relics.enabled", desc, relicsEnabled);

		desc = "Set this to false to disable the 1.8 Stones available as mana alchemy recipes. This only disables the recipes and entries. Old ones that are already in the world will stay.";
		stones18Enabled = loadPropBool("18stones.enabled", desc, stones18Enabled);

		desc = "Set this to false to make the Ring of Odin not apply fire resistance. Mostly for people who use Witchery transformations.";
		ringOfOdinFireResist = loadPropBool("ringOfOdin.fireResist", desc, ringOfOdinFireResist);

		desc = "Set this to false to disable the 1.9 Ender features available as recipes. This only disables the recipes and entries. Old ones that are already in the world will stay.";
		enderStuff19Enabled = loadPropBool("19enderStuff.enabled", desc, enderStuff19Enabled);

		desc = "Set this to true to invert the Ring of Magnetization's controls (from shift to stop to shift to work)";
		invertMagnetRing = loadPropBool("magnetRing.invert", desc, invertMagnetRing);

		desc = "Set this to false to disable Thaumcraft Infusion Stabilizing in botania blocks";
		enableThaumcraftStablizers = loadPropBool("thaumcraftStabilizers.enabled", desc, enableThaumcraftStablizers);
		
		desc = "The harvest level of the Mana Lens: Weight. 3 is diamond level. Defaults to 2 (iron level)";
		harvestLevelWeight = loadPropInt("harvestLevel.weightLens", desc, harvestLevelWeight);

		desc = "The harvest level of the Mana Lens: Bore. 3 is diamond level. Defaults to 3";
		harvestLevelBore = loadPropInt("harvestLevel.boreLens", desc, harvestLevelBore);
		
		desc = "The quantity of Botania flower patches to generate in the world, defaults to 2, the lower the number the less patches generate.";
		flowerQuantity = loadPropInt("worldgen.flower.quantity", desc, flowerQuantity);

		desc = "The amount of time it takes a Passive flower to decay and turn into a dead bush. Defaults to 72000, 60 minutes. Setting this to -1 disables the feature altogether.";
		hardcorePassiveGeneration = loadPropInt("passiveDecay.time", desc, hardcorePassiveGeneration);
		
		desc = "The density of each Botania flower patch generated, defaults to 2, the lower the number, the less each patch will have.";
		adaptor.addMappingInt(0, "worldgen.flower.density", 16);
		adaptor.addMappingInt(238, "worldgen.flower.density", 2);
		flowerDensity = loadPropInt("worldgen.flower.density", desc, flowerDensity);

		desc = "The size of each Botania flower patch, defaults to 6. The larger this is the farther the each patch can spread";
		flowerPatchSize = loadPropInt("worldgen.flower.patchSize", desc, flowerPatchSize);

		desc = "The inverse chance for a Botania flower patch to be generated, defaults to 16. The higher this value is the less patches will exist and the more flower each will have.";
		adaptor.addMappingInt(0, "worldgen.flower.patchChance", 4);
		adaptor.addMappingInt(238, "worldgen.flower.patchChance", 16);
		flowerPatchChance = loadPropInt("worldgen.flower.patchChance", desc, flowerPatchChance);

		desc = "The chance for a Botania flower generated in a patch to be a tall flower. 0.1 is 10%, 1 is 100%. Defaults to 0.05";
		adaptor.addMappingDouble(0, "worldgen.flower.tallChance", 0.1);
		adaptor.addMappingDouble(238, "worldgen.flower.tallChance", 0.05);
		flowerTallChance = loadPropDouble("worldgen.flower.tallChance", desc, flowerTallChance);

		desc = "The quantity of Botania mushrooms to generate underground, in the world, defaults to 40, the lower the number the less patches generate.";
		mushroomQuantity = loadPropInt("worldgen.mushroom.quantity", desc, mushroomQuantity);

		potionIDSoulCross = loadPropPotionId(LibPotionNames.SOUL_CROSS, potionIDSoulCross);
		potionIDFeatherfeet = loadPropPotionId(LibPotionNames.FEATHER_FEET, potionIDFeatherfeet);
		potionIDEmptiness = loadPropPotionId(LibPotionNames.EMPTINESS, potionIDEmptiness);
		potionIDBloodthirst = loadPropPotionId(LibPotionNames.BLOODTHIRST, potionIDBloodthirst);
		potionIDAllure = loadPropPotionId(LibPotionNames.ALLURE, potionIDAllure);
		potionIDClear = loadPropPotionId(LibPotionNames.CLEAR, potionIDClear);

		if(config.hasChanged())
			config.save();
	}

	public static void loadPostInit() 
	{
		SheddingHandler.loadFromConfig(config);

		if(config.hasChanged())
		{
			config.save();
		}
	}

	public static int loadPropInt(String propName, String desc, int default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;

		if(adaptor != null)
			adaptor.adaptPropertyInt(prop, prop.getInt(default_));

		return prop.getInt(default_);
	}

	public static double loadPropDouble(String propName, String desc, double default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;

		if(adaptor != null)
			adaptor.adaptPropertyDouble(prop, prop.getDouble(default_));

		return prop.getDouble(default_);
	}

	//old load prop with no category

	
	public static boolean loadPropBool(String propName, String desc, boolean default_) {
		Property prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_);
		prop.comment = desc;

		if(adaptor != null)
			adaptor.adaptPropertyBool(prop, prop.getBoolean(default_));

		return prop.getBoolean(default_);
	}


	public static boolean loadPropBoolShaders(String propName, String desc, boolean default_) 
	{
		Property prop = config.get(Categories.CATEGORY_SHADERS, propName, default_);
		prop.comment = desc;

		if(adaptor != null)
			adaptor.adaptPropertyBool(prop, prop.getBoolean(default_));

		return prop.getBoolean(default_);
	}


	public static int loadPropPotionId(String propName, int default_) {
		if(!verifiedPotionArray)
			verifyPotionArray();

		Property prop = config.get(CATEGORY_POTIONS, propName, default_);
		int val = prop.getInt(default_);
		if(val > potionArrayLimit) {
			val = default_;
			prop.set(default_);
		}

		return val;
	}

	private static void verifyPotionArray() {
		if(Loader.isModLoaded("DragonAPI"))
			potionArrayLimit = Potion.potionTypes.length;
		else potionArrayLimit = 127;

		verifiedPotionArray = true;
	}

	public static class ConfigAdaptor {

		private boolean enabled;
		private int lastBuild;
		private int currentBuild;

		private Map<String, List<AdaptableValue>> adaptableValues = new HashMap();
		private List<String> changes = new ArrayList();

		public ConfigAdaptor(boolean enabled) {
			this.enabled = enabled;

			String lastVersion = Botania.proxy.getLastVersion();
			try {
				lastBuild = Integer.parseInt(lastVersion);
				currentBuild = Integer.parseInt(LibMisc.BUILD);
			} 
			catch(NumberFormatException e) {
				this.enabled = false;
			}
		}

		public <T> void adaptProperty(Property prop, T val) {
			if(!enabled)
				return;

			String name = prop.getName();

			if(!adaptableValues.containsKey(name))
				return;

			AdaptableValue<T> bestValue = null;
			for(AdaptableValue<T> value : adaptableValues.get(name)) {
				if(value.version >= lastBuild) // If version is newer than what we last used we don't care about it
					continue;

				if(bestValue == null || value.version > bestValue.version)
					bestValue = value;
			}

			if(bestValue != null) {
				T expected = bestValue.value;
				T def = (T) prop.getDefault();
				
				if(areEqualNumbers(val, expected) && !areEqualNumbers(val, def)) {
					prop.setValue(def.toString());
					changes.add(" " + prop.getName() + ": " + val + " -> " + def);
				}
			}
		}

		public <T> void addMapping(int version, String key, T val) {
			if(!enabled)
				return;

			AdaptableValue<T> adapt = new AdaptableValue<T>(version, val);
			if(!adaptableValues.containsKey(key)) {
				ArrayList list = new ArrayList();
				adaptableValues.put(key, list);
			}

			List<AdaptableValue> list = adaptableValues.get(key);
			list.add(adapt);
		}
		
		public boolean areEqualNumbers(Object v1, Object v2) {
			double epsilon = 1.0E-6;
			float v1f = ((Number) v1).floatValue();
			float v2f;
			
			if(v2 instanceof String)
				v2f = Float.parseFloat((String) v2);
			else v2f = ((Number) v2).floatValue();

			return Math.abs(v1f - v2f) < epsilon;
		}

		public void tellChanges(EntityPlayer player) {
			if(changes.size() == 0)
				return;

			player.addChatComponentMessage(new ChatComponentTranslation("botaniamisc.adaptativeConfigChanges").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
			for(String change : changes)
				player.addChatMessage(new ChatComponentText(change).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE)));
		}

		public void addMappingInt(int version, String key, int val) {
			this.<Integer>addMapping(version, key, val);
		}

		public void addMappingDouble(int version, String key, double val) {
			this.<Double>addMapping(version, key, val);
		}

		public void addMappingBool(int version, String key, boolean val) {
			this.<Boolean>addMapping(version, key, val);
		}

		public void adaptPropertyInt(Property prop, int val) {
			this.<Integer>adaptProperty(prop, val);
		}

		public void adaptPropertyDouble(Property prop, double val) {
			this.<Double>adaptProperty(prop, val);
		}

		public void adaptPropertyBool(Property prop, boolean val) {
			this.<Boolean>adaptProperty(prop, val);
		}

		public static class AdaptableValue<T> {

			public final int version;
			public final T value;
			public final Class<? extends T> valueType;

			public AdaptableValue(int version, T value) {
				this.version = version;
				this.value = value;
				valueType = (Class<? extends T>) value.getClass();
			}

		}

	}

	public static class ChangeListener {

		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
			if(eventArgs.modID.equals(LibMisc.MOD_ID))
				load();
		}

	}
}
