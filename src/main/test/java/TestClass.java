import com.zeitheron.hammercore.asm.HammerCoreTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class TestClass
{
	public static void testBytecode()
	{
		System.out.println("hi?");
	}

	public static void main(String[] args)
	{
		File in = new File("D:\\$Code\\Minecraft\\Commissions\\15 - .. HBM Fixes\\out\\test\\15 - .. HBM Fixes\\TestClass.class");

		try
		{
			byte[] basicClass = Files.readAllBytes(in.toPath());
			ClassNode node = new ClassNode();
			ClassReader classReader = new ClassReader(basicClass);
			classReader.accept(node, 0);

			for(MethodNode method : node.methods)
			{
				if(method.name.equals("testBytecode"))
				{
					System.out.println("Desc: " + method.desc);
					System.out.println("Access: " + method.access);
					System.out.println("------------- INSTRUCTION -------------");
					InsnList insn = method.instructions;

					Map<Label, String> labelNames = new HashMap<>();
					for(AbstractInsnNode i : insn.toArray())
					{
						if(i instanceof LabelNode)
						{
							LabelNode ln = (LabelNode) i;
							labelNames.put(ln.getLabel(), "label" + (labelNames.size() + 1));
						}
					}

					for(AbstractInsnNode i : insn.toArray())
					{
						String parsed = "";

						if(i instanceof LabelNode)
						{
							parsed = ", label=" + labelNames.get(((LabelNode) i).getLabel());
						}

						if(i instanceof MethodInsnNode)
						{
							MethodInsnNode m = (MethodInsnNode) i;
							parsed = ", owner=" + m.owner + ", name=" + m.name + ", desc=" + m.desc + ", itf=" + m.itf;
						}

						if(i instanceof JumpInsnNode)
						{
							JumpInsnNode j = (JumpInsnNode) i;
							parsed = ", label=" + labelNames.get(j.label.getLabel());
						}

						if(i instanceof LineNumberNode)
						{
							parsed = ", line=" + ((LineNumberNode) i).line;
						}

						if(i instanceof VarInsnNode)
						{
							parsed = ", var=" + ((VarInsnNode) i).var;
						}

						System.out.println(i.getClass().getSimpleName() + "(" + HammerCoreTransformer.opcodeName(i.getOpcode()) + parsed + ")");
					}
					System.out.println("---------------------------------------");
				}
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}

		System.exit(0);
	}
}