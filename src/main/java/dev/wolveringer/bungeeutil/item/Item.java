package dev.wolveringer.bungeeutil.item;

import java.util.LinkedHashMap;
import java.util.Map;

import dev.wolveringer.bungeeutil.item.meta.ItemMeta;
import dev.wolveringer.bungeeutil.item.meta.MetaFactory;
import dev.wolveringer.configuration.serialization.ConfigurationSerializable;
import dev.wolveringer.nbt.NBTTagCompound;

public class Item{
	/**
	 * Required method for configuration serialization
	 *
	 * @param args
	 *            map to deserialize
	 * @return deserialized item stack
	 * @see ConfigurationSerializable
	 */
	public static Item deserialize(Map<String, Object> args) {
		Material type = Material.getMaterial((String) args.get("type"));
		short damage = 0;
		int amount = 1;

		if(args.containsKey("damage")){
			damage = ((Number) args.get("damage")).shortValue();
		}

		if(args.containsKey("amount")){
			amount = (Integer) args.get("amount");
		}

		Item result = new Item(type, amount, damage);

		if(args.containsKey("meta")){ // We cannot and will not have meta when enchantments (pre-ItemMeta) exist
			Object raw = args.get("meta");
			if(raw instanceof ItemMeta){
				result.setTag(((ItemMeta) raw).getTag());
			}
		}

		return result;
	}

	@SuppressWarnings("deprecation")
	private static Material getType0(int id) {
		Material material = Material.getMaterial(id);
		return material == null ? Material.AIR : material;
	}

	private int amount = 0;
	private MaterialData data = null;
	private short durability = 0;
	private ItemMeta meta;
	private NBTTagCompound tag;
	private int type = 0;

	/**
	 * Defaults stack size to 1, with no extra data
	 *
	 * @param type
	 *            item material id
	 * @deprecated Magic value
	 */
	@Deprecated
	public Item(final int type) {
		this(type, 1);
	}

	/**
	 * An item stack with no extra data
	 *
	 * @param type
	 *            item material id
	 * @param amount
	 *            stack size
	 * @deprecated Magic value
	 */
	@Deprecated
	public Item(final int type, final int amount) {
		this(type, amount, (short) 0);
	}

	/**
	 * An item stack with the specified damage / durability
	 *
	 * @param type
	 *            item material id
	 * @param amount
	 *            stack size
	 * @param damage
	 *            durability / damage
	 * @deprecated Magic value
	 */
	@Deprecated
	public Item(final int type, final int amount, final short damage) {
		this.type = type;
		this.amount = amount;
		this.durability = damage;
		this.meta = MetaFactory.getItemMeta(this);
	}

	/**
	 * @deprecated this method uses an ambiguous data byte object
	 */
	@Deprecated
	public Item(final int type, final int amount, final short damage, final Byte data) {
		this.type = type;
		this.amount = amount;
		this.durability = damage;
		if(data != null){
			this.createData(data);
			this.durability = data;
		}
		this.meta = MetaFactory.getItemMeta(this);
	}

	/**
	 * Creates a new item stack derived from the specified stack
	 *
	 * @param stack
	 *            the stack to copy
	 * @throws IllegalArgumentException
	 *             if the specified stack is null or
	 *             returns an item meta not created by the item factory
	 */
	public Item(final Item stack) throws IllegalArgumentException {
		this.type = stack.getTypeId();
		this.amount = stack.getAmount();
		this.durability = stack.getDurability();
		this.data = stack.getData();
		this.tag = stack.tag;
		this.meta = this.getItemMeta();
	}

	/**
	 * Defaults stack size to 1, with no extra data
	 *
	 * @param type
	 *            item material
	 */
	public Item(final Material type) {
		this(type, 1);
	}

	/**
	 * An item stack with no extra data
	 *
	 * @param type
	 *            item material
	 * @param amount
	 *            stack size
	 */
	public Item(final Material type, final int amount) {
		this(type.getId(), amount);
	}

	/**
	 * An item stack with the specified damage / durabiltiy
	 *
	 * @param type
	 *            item material
	 * @param amount
	 *            stack size
	 * @param damage
	 *            durability / damage
	 */
	public Item(final Material type, final int amount, final short damage) {
		this((type == null ? Material.AIR : type).getId(), amount, damage);
	}

	/**
	 * @deprecated this method uses an ambiguous data byte object
	 */
	@Deprecated
	public Item(final Material type, final int amount, final short damage, final Byte data) {
		this(type.getId(), amount, damage, data);
	}

	@SuppressWarnings("deprecation")
	private void createData(final byte data) {
		Material mat = Material.getMaterial(this.type);
		if(mat == null){
			this.data = new MaterialData(this.type, data);
		}else{
			this.data = new MaterialData(data);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(!(obj instanceof Item)){
			return false;
		}

		Item stack = (Item) obj;
		return this.getAmount() == stack.getAmount() && this.isSimilar(stack);
	}

	/**
	 * Gets the amount of items in this stack
	 *
	 * @return Amount of items in this stick
	 */
	public int getAmount() {
		return this.amount;
	}

	/**
	 * Gets the MaterialData for this stack of items
	 *
	 * @return MaterialData for this item
	 */
	public MaterialData getData() {
		Material mat = this.getType();
		if(this.data == null && mat != null){
			this.data = new MaterialData(this.getDurability());
		}

		return this.data;
	}

	/**
	 * Gets the durability of this item
	 *
	 * @return Durability of this item
	 */
	public short getDurability() {
		return this.durability;
	}

	/**
	 * Get a copy of this ItemStack's {@link ItemMeta}.
	 *
	 * @return a copy of the current ItemStack's ItemData
	 */
	public ItemMeta getItemMeta() {
		return this.meta == null ? this.meta = MetaFactory.getItemMeta(this) : this.meta;
	}

	/**
	 * Get the maximum stacksize for the material hold in this ItemStack.
	 * (Returns -1 if it has no idea)
	 *
	 * @return The maximum you can stack this material to.
	 */
	public int getMaxStackSize() {
		Material material = this.getType();
		if(material != null){
			return material.getMaxStackSize();
		}
		return -1;
	}

	public NBTTagCompound getTag() {
		return this.tag;
	}

	/**
	 * Gets the type of this item
	 *
	 * @return Type of the items in this stack
	 */
	public Material getType() {
		return getType0(this.getTypeId());
	}

	/**
	 * Gets the type id of this item
	 *
	 * @return Type Id of the items in this stack
	 * 
	 */
	public int getTypeId() {
		return this.type;
	}

	@Override
	public final int hashCode() {
		int hash = 1;

		hash = hash * 31 + this.getTypeId();
		hash = hash * 31 + this.getAmount();
		hash = hash * 31 + (this.getDurability() & 0xffff);
		hash = hash * 31 + (this.hasItemMeta() ? this.meta == null ? this.getItemMeta().hashCode() : this.meta.hashCode() : 0);

		return hash;
	}

	/**
	 * Checks to see if any meta data has been defined.
	 *
	 * @return Returns true if some meta data has been set for this item
	 */
	public boolean hasItemMeta() {
		return !MetaFactory.equals(this.meta, null);
	}

	public boolean hasTag() {
		return this.meta==null||this.meta.hasTag();
	}

	/**
	 * This method is the same as equals, but does not consider stack size
	 * (amount).
	 *
	 * @param stack
	 *            the item stack to compare to
	 * @return true if the two stacks are equal, ignoring the amount
	 */
	public boolean isSimilar(Item stack) {
		if(stack == null){
			return false;
		}
		if(stack == this){
			return true;
		}
		return this.getTypeId() == stack.getTypeId() && this.getDurability() == stack.getDurability() && MetaFactory.equals(this.getItemMeta(), stack.getItemMeta());
	}

	public Map<String, Object> serialize() {
		Map<String, Object> result = new LinkedHashMap<String, Object>();

		result.put("type", this.getType().name());

		if(this.getDurability() != 0){
			result.put("damage", this.getDurability());
		}

		if(this.getAmount() != 1){
			result.put("amount", this.getAmount());
		}

		ItemMeta meta = this.getItemMeta();
		if(!MetaFactory.equals(meta, null)){
			result.put("meta", meta);
		}
		return result;
	}

	/**
	 * Sets the amount of items in this stack
	 *
	 * @param amount
	 *            New amount of items in this stack
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Sets the MaterialData for this stack of items
	 *
	 * @param data
	 *            New MaterialData for this item
	 */
	public void setData(MaterialData data) {
		this.getType();
		this.data = data;
	}

	/**
	 * Sets the durability of this item
	 *
	 * @param durability
	 *            Durability of this item
	 */
	public void setDurability(final short durability) {
		this.durability = durability;
	}

	public void setTag(NBTTagCompound b) {
		this.tag = b;
	}

	/**
	 * Sets the type of this item
	 * <p>
	 * Note that in doing so you will reset the MaterialData for this stack
	 *
	 * @param type
	 *            New type to set the items in this stack to
	 */
	public void setType(Material type) {
		this.setTypeId(type.getId());
	}

	/**
	 * Sets the type id of this item
	 * <p>
	 * Note that in doing so you will reset the MaterialData for this stack
	 *
	 * @param type
	 *            New type id to set the items in this stack to
	 *            
	 */
	public void setTypeId(int type) {
		this.type = type;
		this.createData((byte) 0);
	}

	@Override
	public String toString() {
		return "Item@" + System.identityHashCode(this) + "[amount=" + this.amount + ", data=" + this.data + ", durability=" + this.durability + ", meta=" + this.meta + ", tag=" + this.tag + ", type=" + this.type + "]";
	}
}
