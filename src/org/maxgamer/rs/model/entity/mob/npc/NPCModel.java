package org.maxgamer.rs.model.entity.mob.npc;

import java.io.IOException;

import org.maxgamer.rs.io.OutputStreamWrapper;
import org.maxgamer.rs.model.entity.mob.MobModel;

/**
 * @author netherfoam
 */
public class NPCModel extends MobModel {
	private int id;
	
	public NPCModel(int id){
		this.id = id;
	}
	
	public NPCModel(NPCDefinition def) {
		this(def.getId());
		
		this.setCombatLevel(def.getCombatLevel());
	}
	
	@Override
	protected void appendUpdate(OutputStreamWrapper out) throws IOException {
		out.writeShort(-1); //Mob type or something?
		out.writeShort(id);
		out.writeByte(0); //Unknown
	}
	
	
	public int getModelId() {
		return id;
	}
	
	public void getModelId(int id) {
		this.id = id;
		setChanged(true);
	}
	
}