function initializeCoreMod() {
    return {
        'coremodmethod': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.tileentity.AbstractFurnaceTileEntity',
                'methodName': 'func_213997_a',
                'methodDesc': '(Lnet/minecraft/item/ItemStack;)I'
            },
            'transformer': function(method) {
                print('[FastFurnace]: Patching AbstractFurnaceTileEntity#getBurnTime');

                var owner = "shadows/fastfurnace/FastFurnace";
                var name = "getBurnTime";
                var desc = "(Lnet/minecraft/item/ItemStack;)I";
                var instr = method.instructions;

                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var insn = new InsnList();
                insn.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insn.add(ASMAPI.buildMethodCall(
                    owner,
                    name,
                    desc,
                    ASMAPI.MethodType.STATIC));
                insn.add(new InsnNode(Opcodes.IRETURN));
                instr.insert(insn);

                return method;
            }
        }
    }
}