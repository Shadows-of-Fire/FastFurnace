function initializeCoreMod() {
    return {
        'coremodmethod': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.network.play.ClientPlayNetHandler',
                'methodName': 'func_199723_a',
                'methodDesc': '(Lnet/minecraft/network/play/server/STagsListPacket;)V'
            },
            'transformer': function(method) {
                print('[FastFurnace]: Patching ClientPlayNetHandler#handleTags');

                var owner = "shadows/fastfurnace/FastFurnace";
                var name = "updateBurns";
                var desc = "()V";
                var instr = method.instructions;

                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
				
                instr.add(ASMAPI.buildMethodCall(
                    owner,
                    name,
                    desc,
                    ASMAPI.MethodType.STATIC));

                return method;
            }
        }
    }
}