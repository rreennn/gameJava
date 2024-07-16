public class Enemy {
    private String name;
    private int maxHp;
    private int hp;
    private int atk;
    private int def;
    private int damage;
    private int defense;

    public Enemy(String name, int maxHp, int hp, int atk, int def) {
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
    }

    public String getName() {
        return name;
    }

    public int getAtk() {
        return atk;
    }

    public int getHp() {
        return hp;
    }

    public int getDef() {
        return def;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    //function attack, defend (more like heal)
    public void attack(int enemyAtk) {
        setDamage(enemyAtk);
    }

    //defend cooldown is 3 turns
    public void defend(int enemyDef) {
        setDefense(enemyDef);
    }

}
