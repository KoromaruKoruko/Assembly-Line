package assemblyline.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import assemblyline.client.ClientRegister;
import assemblyline.common.tile.TileMobGrinder;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;

public class RenderMobGrinder implements BlockEntityRenderer<TileMobGrinder> {
	public RenderMobGrinder(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(TileMobGrinder tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		double progress = tileEntityIn.clientProgress > 0 ? (tileEntityIn.getLevel().getDayTime() + (tileEntityIn.clientProgress > 0 ? partialTicks : 0)) * 30 : 0;
		BakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MOBGRINDERSIDEWHEEL);
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, 2.5 / 16.0);
		matrixStackIn.mulPose(new Quaternion((float) -progress, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, -2.5 / 16.0);
		matrixStackIn.mulPose(new Quaternion((float) progress, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MOBGRINDERCENTERWHEEL);
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.mulPose(new Quaternion(0, 0, 90, true));
		matrixStackIn.translate(1.0 / 16.0, 6.0 / 16.0, 0);
		matrixStackIn.mulPose(new Quaternion((float) progress, 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		matrixStackIn.pushPose();
		matrixStackIn.translate(0, 1.0 / 16.0, 0);
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.mulPose(new Quaternion(-90, 0, 90, true));
		ibakedmodel = Minecraft.getInstance().getModelManager().getModel(ClientRegister.MODEL_MOBGRINDERBASE);
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.translate(0, progress / 8.0 - 1 / 8.0, 0);
		matrixStackIn.popPose();
	}
}
