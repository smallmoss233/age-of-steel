package amble.aos.client.models.entity;

import amble.aos.client.animations.MondasianCybermanAnimations;
import amble.aos.core.entities.CybermanBase;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3f;

public class MondasianCybermanModel<T extends CybermanBase> extends HierarchicalModel<T> {
	private static final Vector3f ANIM_SCRATCH = new Vector3f();

	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart Body;
	private final ModelPart lazergun;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	public MondasianCybermanModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.head = this.bone.getChild("head");
		this.Body = this.bone.getChild("Body");
		this.lazergun = this.Body.getChild("lazergun");
		this.RightArm = this.bone.getChild("RightArm");
		this.LeftArm = this.bone.getChild("LeftArm");
		this.RightLeg = this.bone.getChild("RightLeg");
		this.LeftLeg = this.bone.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.13F))
		.texOffs(0, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.4F))
		.texOffs(40, 0).addBox(4.4F, -11.9F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.1F))
		.texOffs(40, 0).mirror().addBox(-6.4F, -11.9F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false)
		.texOffs(24, 20).mirror().addBox(-4.6F, -11.9F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false)
		.texOffs(24, 20).addBox(2.6F, -11.9F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F))
		.texOffs(0, 20).addBox(2.29F, -3.9F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
		.texOffs(0, 20).mirror().addBox(-4.29F, -3.9F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false)
		.texOffs(32, 26).addBox(-3.0F, -14.4F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(31, 16).addBox(-1.0F, -12.4F, 2.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-3.0F, -14.4F, -2.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition Body = bone.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(32, 39).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 6).addBox(-1.0F, 0.3F, -3.7F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(43, 12).addBox(1.0F, 0.3F, -3.7F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(43, 12).mirror().addBox(-3.0F, 0.3F, -3.7F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(28, 55).addBox(-3.0F, 0.3F, -2.011F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(16, 48).addBox(-3.0F, -0.2F, -2.7F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(56, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition lazergun = Body.addOrReplaceChild("lazergun", CubeListBuilder.create().texOffs(51, 16).addBox(-2.0F, -16.0502F, 3.067F, 4.0F, 4.0F, 2.0F, new CubeDeformation(-0.3F))
		.texOffs(52, 38).addBox(-2.0F, -16.0502F, 3.667F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, 24.2502F, -6.967F));

		PartDefinition Body_r1 = lazergun.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(50, 25).addBox(-4.0F, -2.0F, 1.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.4871F, 2.9433F, 0.3491F, 0.0F, 0.0F));

		PartDefinition Body_r2 = lazergun.addOrReplaceChild("Body_r2", CubeListBuilder.create().texOffs(50, 25).addBox(-4.0F, -2.0F, 1.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.4124F, 2.9433F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightArm = bone.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(48, 55).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 64).mirror().addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false)
		.texOffs(0, 64).mirror().addBox(-3.0F, -1.8F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(-5.0F, -22.0F, 0.0F, -0.0038F, -0.0872F, 0.0874F));

		PartDefinition LeftArm = bone.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(48, 55).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 64).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F))
		.texOffs(0, 64).addBox(-1.0F, -1.8F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(5.0F, -22.0F, 0.0F, -0.0038F, 0.0872F, -0.0874F));

		PartDefinition RightLeg = bone.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 64).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-1.9F, -12.0F, 0.0F, 0.0F, 0.0436F, 0.0436F));

		PartDefinition LeftLeg = bone.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(1.9F, -12.0F, 0.0F, 0.0F, -0.0436F, -0.0436F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart root() {
		return this.bone;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		AnimationDefinition walk = entity.isChasing()
				? MondasianCybermanAnimations.SPEED_WALK
				: MondasianCybermanAnimations.WALKING;
		if (entity.isMarching()) {
			float amplitude = Math.min(limbSwingAmount * CybermanBase.WALK_ANIM_AMPLITUDE, 1.0F);
			float partial = ageInTicks - entity.tickCount;
			double phase = ((entity.level().getGameTime() + partial) * CybermanBase.WALK_CADENCE / 20.0D)
					% walk.lengthInSeconds();
			KeyframeAnimations.animate(this, walk, (long) (phase * 1000.0D), amplitude, ANIM_SCRATCH);
		} else {
			this.animateWalk(walk, limbSwing, limbSwingAmount,
					CybermanBase.WALK_ANIM_SPEED, CybermanBase.WALK_ANIM_AMPLITUDE);
		}

		this.animate(entity.deleteStartAnimationState, MondasianCybermanAnimations.START_DELETE, ageInTicks);
		this.animate(entity.deleteLoopAnimationState, MondasianCybermanAnimations.LOOP_DELETE, ageInTicks);

		this.playOnce(entity.gunAnimationState, MondasianCybermanAnimations.GUN, ageInTicks);
	}

	private void playOnce(AnimationState state, AnimationDefinition animation, float ageInTicks) {
		this.animate(state, animation, ageInTicks);
		if (state.isStarted() && state.getAccumulatedTime() >= (long) (animation.lengthInSeconds() * 1000.0F)) {
			state.stop();
		}
	}
}