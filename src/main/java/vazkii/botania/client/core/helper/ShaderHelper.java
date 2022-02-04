/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 9, 2014, 11:20:26 PM (GMT)]
 * 
 * ==============================================
 * Last Edited: 2/3/22 by Cal Reveraster
 *	• Added support for more modern OpenGL
 *  • Added initial logic for individual shader envocation
 * ==============================================
 *
 */

 /*
 * Shaders brought up to gl 2.0 and off of... 10-14 year old implementations. 
 * These implements were old even when 1.7 was in its prime. 
 * Probably why it's not interfacing well with shaders. Hopefully. 
 */

package vazkii.botania.client.core.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.minecraft.client.renderer.OpenGlHelper;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

//new opengl import(s)
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;
import cpw.mods.fml.common.FMLLog;

public final class ShaderHelper 
{

	//variables
	private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

	public static int pylonGlow = 0;
	public static int enchanterRune = 0;
	public static int manaPool = 0;
	public static int doppleganger = 0;
	public static int halo = 0;
	public static int dopplegangerBar = 0;
	public static int terraPlateRune = 0;
	public static int filmGrain = 0;
	public static int gold = 0;
	public static int categoryButton = 0;

	/*
	*============================================================================
	*                 INIT
	*============================================================================
	*/

	public static void initShaders() 
	{
		FMLLog.log(Level.INFO, "[BOTANIA] Shaders system initializing...");

		//We can remove this soon, as the system will only enable shaders that it needs. 
		//if the main shader config isn't on, break out.
		//if(!shadersAreUsable())
		//{
			//FMLLog.log(Level.INFO, "[BOTANIA] Shaders disabled.");
			//return;
		//}
		
		/*
		* Implemented individual disablers at the config level. 
		* No idea if this will work. Guess we'll find out.
		*/

		//if (pylonGlowUsable())
		//{
			FMLLog.log(Level.INFO, "[BOTANIA] Starting Pylong Glow shader...");
			pylonGlow = createProgram(null, LibResources.SHADER_PYLON_GLOW_FRAG);
		//}

		//if (enchanterRuneUsable())
		//{
			FMLLog.log(Level.INFO, "[BOTANIA] Starting Enchanter Rune shader...");
			enchanterRune = createProgram(null, LibResources.SHADER_ENCHANTER_RUNE_FRAG);
		//}

		//if (manaPoolUsable())
		//{
			FMLLog.log(Level.INFO, "[BOTANIA] Starting Mana Pool shader...");
			manaPool = createProgram(null, LibResources.SHADER_MANA_POOL_FRAG);
		//}

		//if (dopplegangerUsable())
		//{
			FMLLog.log(Level.INFO, "[BOTANIA] Starting Doppleganger shader...");
			doppleganger = createProgram(LibResources.SHADER_DOPLLEGANGER_VERT, LibResources.SHADER_DOPLLEGANGER_FRAG);
		//}

		//if (haloUsable())
		//{
			FMLLog.log(Level.INFO, "[BOTANIA] Starting Halo shader...");
			halo = createProgram(null, LibResources.SHADER_HALO_FRAG);
		//}

		//if (dopplegangerBarUsable())
		//{
			FMLLog.log(Level.INFO, "[BOTANIA] Starting Bar shader...");
			dopplegangerBar = createProgram(null, LibResources.SHADER_DOPLLEGANGER_BAR_FRAG);
		//}

		//if (terraplateRuneUsable())
		//{
			FMLLog.log(Level.INFO, "[BOTANIA] Starting Terraplate shader...");
			terraPlateRune = createProgram(null, LibResources.SHADER_TERRA_PLATE_RUNE_FRAG);
		//}

		//if (filmGrainUsable())
		//{
			filmGrain = createProgram(null, LibResources.SHADER_FILM_GRAIN_FRAG);
		//}

		//if (goldUsable())
		//{
			gold = createProgram(null, LibResources.SHADER_GOLD_FRAG);
		//}

		//if (categoryButtonUsable())
		//{
			categoryButton = createProgram(null, LibResources.SHADER_CATEGORY_BUTTON_FRAG);
		//}
	}

	/*
	*============================================================================
	*                 useShader
	*============================================================================
	*/

	//==============================================
	//		GENERAL SHADER ENVOKER
	//==============================================

	//GL20 done
	public static void useShader(int shader, ShaderCallback callback) 
	{
		if(!shadersAreUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] SOMETHING JUST TRIGGERED THE GENERAL SHADER ENVOKER. YOU MISSED SOMETHING.");
		GL20.glUseProgram(shader);
		
		//ARBShaderObjects.glUseProgramObjectARB(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useShader(int shader) 
	{
		useShader(shader, null);
	}

	//==============================================
	//		PYLON GLOW ENVOKER
	//		(Implemented)
	//==============================================
	
	public static void usePylonGlowShader(int shader, ShaderCallback callback)
	{
		if(!pylonGlowUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] PYLON GLOW ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void usePylonGlowShader(int shader) 
	{
		usePylonGlowShader(shader, null);
	}

	//==============================================
	//		MANA POOL ENVOKER
	//		(Implemented)
	//==============================================

	public static void useManaPoolShader(int shader, ShaderCallback callback)
	{
		if(!manaPoolUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] MANA POOL ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useManaPoolShader(int shader) 
	{
		useManaPoolShader(shader, null);
	}

	//==============================================
	//		ENCHANTER RUNE ENVOKER
	//		(implemented)
	//==============================================

	public static void useEnchanterRuneShader(int shader, ShaderCallback callback)
	{
		if(!enchanterRuneUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] ENCHANTER RUNE ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useEnchanterRuneShader(int shader) 
	{
		useEnchanterRuneShader(shader, null);
	}

	//==============================================
	//		DOPPLEGANGER ENVOKER
	//		(Implemented)
	//==============================================

	public static void useDopplegangerShader(int shader, ShaderCallback callback)
	{
		if(!dopplegangerUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] DOPPLEGANGER ENVOKER TRIGGERED!...");
		//GL20.glUseProgram(shader);
		ARBShaderObjects.glUseProgramObjectARB(shader)

		if(shader != 0) 
		{
			int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			//int time = GL20.glGetUniformLocation(shader, "time");
			//GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useDopplegangerShader(int shader) 
	{
		useDopplegangerShader(shader, null);
	}

	//==============================================
	//		DOPPLEGANGER BAR ENVOKER
	//==============================================

	public static void useDopplegangerBarShader(int shader, ShaderCallback callback)
	{
		if(!dopplegangerBarUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] DOPPLEGANGER BAR ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useDopplegangerBarShader(int shader) 
	{
		useDopplegangerBarShader(shader, null);
	}

	//==============================================
	//		HALO ENVOKER
	//		(implemented)
	//==============================================

	public static void useHaloShader(int shader, ShaderCallback callback)
	{
		if(!haloUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] HALO ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useHaloShader(int shader) 
	{
		useHaloShader(shader, null);
	}

	//==============================================
	//		TERRAPLATE ENVOKER
	//==============================================

	public static void useTerraplateRuneShader(int shader, ShaderCallback callback)
	{
		if(!terraplateRuneUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] TERRAPLATE ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useTerraplateRuneShader(int shader) 
	{
		useTerraplateRuneShader(shader, null);
	}

	//==============================================
	//		FILM GRAIN ENVOKER
	//==============================================

	public static void useFilmGrainShader(int shader, ShaderCallback callback)
	{
		if(!filmGrainUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] FILM GRAIN ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useFilmGrainShader(int shader) 
	{
		useFilmGrainShader(shader, null);
	}

	//==============================================
	//		GOLD ENVOKER
	//==============================================

	public static void useGoldShader(int shader, ShaderCallback callback)
	{
		if(!goldUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] GOLD ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useGoldShader(int shader) 
	{
		useGoldShader(shader, null);
	}

	//==============================================
	//		CATEGORY BUTTON ENVOKER
	//==============================================

	public static void useCategoryButtonShader(int shader, ShaderCallback callback)
	{
		if(!categoryButtonUsable())
		{
			return;
		}

		//FMLLog.log(Level.INFO, "[BOTANIA] CATEGORY BUTTON ENVOKER TRIGGERED!...");
		GL20.glUseProgram(shader);

		if(shader != 0) 
		{
			//int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			//ARBShaderObjects.glUniform1iARB(time, ClientTickHandler.ticksInGame);
			int time = GL20.glGetUniformLocation(shader, "time");
			GL20.glUniform1i(time, ClientTickHandler.ticksInGame);

			if(callback != null)
			{
				callback.call(shader);
			}
		}
	}

	public static void useCategoryButtonShader(int shader) 
	{
		useCategoryButtonShader(shader, null);
	}

	//==============================================
	//		YEET SHADERS
	//==============================================

	public static void releaseShader() 
	{
		useShader(0);
	}

	public static void releasePylonGlowShader() 
	{
		usePylonGlowShader(0);
	}

	public static void releaseManaPoolShader() 
	{
		useManaPoolShader(0);
	}

	public static void releaseEnchanterRuneShader() 
	{
		useEnchanterRuneShader(0);
	}

	public static void releaseDopplegangerShader() 
	{
		useDopplegangerShader(0);
	}

	public static void releaseDopplegangerBarShader() 
	{
		useDopplegangerBarShader(0);
	}

	public static void releaseHaloShader() 
	{
		useHaloShader(0);
	}

	public static void releaseTerraplateRuneShader() 
	{
		useTerraplateRuneShader(0);
	}

	public static void releaseFilmGrainShader() 
	{
		useFilmGrainShader(0);
	}

	public static void releaseGoldShader() 
	{
		useGoldShader(0);
	}

	public static void releaseCategoryButtonShader() 
	{
		useCategoryButtonShader(0);
	}

/*
*============================================================================
*                 SHADER CHECKS
*============================================================================
*/

	//renamed from useShaders to be less confusing
	public static boolean shadersAreUsable() 
	{
		return ConfigHandler.useShaders && OpenGlHelper.shadersSupported;
	}

	public static boolean pylonGlowUsable() 
	{
		return ConfigHandler.usePylonGlowShader && OpenGlHelper.shadersSupported;
	}

	public static boolean enchanterRuneUsable() 
	{
		return ConfigHandler.useEnchanterRuneShader && OpenGlHelper.shadersSupported;
	}

	public static boolean manaPoolUsable() 
	{
		return ConfigHandler.useManaPoolShader && OpenGlHelper.shadersSupported;
	}

	public static boolean dopplegangerUsable() 
	{
		return ConfigHandler.useDopplegangerShader && OpenGlHelper.shadersSupported;
	}

	public static boolean haloUsable() 
	{
		return ConfigHandler.useHaloShader && OpenGlHelper.shadersSupported;
	}

	public static boolean dopplegangerBarUsable() 
	{
		return ConfigHandler.useDopplegangerBarShader && OpenGlHelper.shadersSupported;
	}

	public static boolean terraplateRuneUsable() 
	{
		return ConfigHandler.useTerraplateRuneShader && OpenGlHelper.shadersSupported;
	}

	public static boolean filmGrainUsable() 
	{
		return ConfigHandler.useFilmGrainShader && OpenGlHelper.shadersSupported;
	}

	public static boolean goldUsable() 
	{
		return ConfigHandler.useGoldShader && OpenGlHelper.shadersSupported;
	}

	public static boolean categoryButtonUsable() 
	{
		return ConfigHandler.usePylonGlowShader && OpenGlHelper.shadersSupported;
	}

	/*
	*============================================================================
	*                 CREATE PROGRAM 
	*============================================================================
	*/

	//GL20 done.
	private static int createProgram(String vert, String frag) 
	{
		//set each ID to 0 
		int vertId = 0, fragId = 0, program = 0;

		//if arguments arent null, then set those above numbers to the arguments instead. 
		if(vert != null)
		{
			vertId = createShader(vert, VERT);
		}
		if(frag != null)
		{
			fragId = createShader(frag, FRAG);
		}

		//gl20
		//program = ARBShaderObjects.glCreateProgramObjectARB();
		program = GL20.glCreateProgram();

		if(program == 0)
		{
			return 0;
		}

		if(vert != null)
		{
			GL20.glAttachShader(program, vertId);
			//ARBShaderObjects.glAttachObjectARB(program, vertId);
		}

		if(frag != null)
		{
			GL20.glAttachShader(program, vertId);
			//ARBShaderObjects.glAttachObjectARB(program, fragId);
		}


		//ARBShaderObjects.glLinkProgramARB(program);
		GL20.glLinkProgram(program);

		/*
		* This block checks for compat with an old gl version.

		if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) 
		{
			FMLLog.log(Level.INFO, "What are you running this on, msDOS 6? Is the screen phosphorus?");
			FMLLog.log(Level.ERROR, getLogInfo(program));
			return 0;
		}
		*/
		
		//This is the new check, Ill figure it out later.
		/*

		ontextCapabilities caps = GLContext.getCapabilities();

		if ( caps.OpenGL20 ) 
		{
    		// OpenGL 2.0 requires both vertex and fragment shader functionality.
    		FMLLog.log(Level.INFO, "[BOTANIA] Congrats! You support not-ancient OpenGL!");
		} 
		else 
		{
    		if ( !(caps.GL_ARB_vertex_shader || caps.GL_ARB_fragment_shader) )
			{
				FMLLog.log(Level.INFO, "[BOTANIA] OpenGL Issues. Shaders getting Yeeted.");
				//FMLLog.log(Level.ERROR, getLogInfo(program));
				return 0;
			}
		}
		*/


		//GL20 update
		//ARBShaderObjects.glValidateProgramARB(program);
		GL20.glValidateProgram(program);
		
		//old version of the above check. Can delete later.
		/*
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) 
		{
			FMLLog.log(Level.INFO, "What are you running this on, msDOS 6? Is the screen phosphorus?");
			FMLLog.log(Level.ERROR, getLogInfo(program));
			return 0;
		}
		*/

		return program;
	}

	//gl20 done
	private static int createShader(String filename, int shaderType){
		int shader = 0;
		
		//attempt to set the shader value
		try 
		{
			//GL20
			//shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
			shader = GL20.glCreateShader(shaderType);

			if(shader == 0)
			{
				return 0;
			}

			//GL20
			//ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			//ARBShaderObjects.glCompileShaderARB(shader);
			GL20.glShaderSource(shader, readFileAsString(filename));
			GL20.glCompileShader(shader);

			//gl20
			/*
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
			{
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			}
			*/
			return shader;
		}

		catch(Exception e) 
		{
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			return -1;
		}
	}

	//Not using right now, GL20. Will write a new one later, maybe. 
	/*
	private static String getLogInfo(int obj) 
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	*/

	private static String readFileAsString(String filename) throws Exception 
	{
		StringBuilder source = new StringBuilder();
		InputStream in = ShaderHelper.class.getResourceAsStream(filename);
		Exception exception = null;
		BufferedReader reader;

		if(in == null)
			return "";

		try 
		{
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			Exception innerExc= null;
			try 
			{
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			} 
			catch(Exception exc) 
			{
				exception = exc;
			} 
			finally 
			{
				try 
				{
					reader.close();
				} 
				catch(Exception exc) 
				{
					if(innerExc == null)
						innerExc = exc;
					else exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		} 
		catch(Exception exc) 
		{
			exception = exc;
		} 
		finally 
		{
			try 
			{
				in.close();
			} 
			catch(Exception exc) 
			{
				if(exception == null)
					exception = exc;
				else exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}
		return source.toString();
	}

}
