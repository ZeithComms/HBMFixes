package org.zeith.zhbmfixes.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class HMBFixesCoremod
		implements IFMLLoadingPlugin, IClassTransformer
{
	public static final Logger LOG = LogManager.getLogger("CustomMobTargets [PLUGIN]");

	public HMBFixesCoremod()
	{
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{ getClass().getName() };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass()
	{
		return null;
	}

	static File asm = new File(".", "asm");

	@Override
	public void injectData(Map<String, Object> data)
	{
		File dir = (File) data.get("mcLocation");
		asm = new File(dir, "asm");
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(transformedName.equals("com.hbm.main.ModEventHandler"))
			return handleHBMModEventHandler(transformedName, basicClass);
		if(transformedName.equals("com.hbm.handler.WeightedRandomChestContentFrom1710"))
			return handleHBMWeightedRandomChestContentFrom1710(transformedName, basicClass);
		return basicClass;
	}

	private byte[] handleHBMWeightedRandomChestContentFrom1710(String name, byte[] bytes)
	{
		return transform(name, bytes, node ->
		{
			for(MethodNode method : node.methods)
			{
				if(method.name.equals("generateChestContents") && method.desc.contains("ICapabilityProvider"))
				{
					LOG.info(" - Found generateChestContents with ICapabilityProvider (probaby a TileEntity), injecting return instructions.");

					InsnList lst = new InsnList();

					lst.add(new VarInsnNode(Opcodes.ALOAD, 0));
					lst.add(new VarInsnNode(Opcodes.ALOAD, 1));
					lst.add(new VarInsnNode(Opcodes.ALOAD, 2));
					lst.add(new VarInsnNode(Opcodes.ILOAD, 3));
					lst.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/zeith/zhbmfixes/asm/ASM", "generateChestContents", method.desc, false));
					lst.add(new InsnNode(Opcodes.RETURN));

					method.instructions = lst;
				}
			}
		});
	}

	private byte[] handleHBMModEventHandler(String name, byte[] bytes)
	{
		return transform(name, bytes, node ->
		{
			node.methods.removeIf(method ->
			{
				if(method.name.equals("mobSpawn"))
				{
					LOG.info("Deleting ModEventHandler.mobSpawn method.");
					return true;
				}
				return false;
			});
		});
	}

	private static byte[] transform(String name, byte[] bytes, Consumer<ClassNode> handler)
	{
		LOG.info("ASMing into " + name);
		ClassNode node = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(node, 0);
		handler.accept(node);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		byte[] data = writer.toByteArray();
		if(asm.isDirectory())
			try(FileOutputStream fos = new FileOutputStream(new File(asm, name + ".class")))
			{
				fos.write(data);
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		return data;
	}
}