
public class Hero {
    private String name;
    private int maxHp;
    private int atk;
    private int currentHp;
    private int def;
    private int exp;
    private int level;
    private int damage;
    private int score;
    private int defense;
    public boolean runAway;
    private int skillCooldown = 0;

    public Hero(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSkillCooldown() {
        return skillCooldown;
    }

    public void setSkillCooldown(int skillCooldown) {
        this.skillCooldown = skillCooldown;
    }
    
    public boolean isAlive() {
        if(currentHp > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setRunAway(boolean runAway) {
        this.runAway = runAway;
    }

    //basic atk damage is flat atk
    public void basicAtk(int heroAtk) {
        setDamage(heroAtk);
        if(skillCooldown > 0) {
            skillCooldown -= 1;
        }
    }

    //skill atk damage is flat atk + 0.5 * flat atk
    //notes skill atk cooldown is 3 turns
    public void skillAtk(int heroAtk) {
        if(skillCooldown == 0) {
            double damages = heroAtk * 0.5;
            setDamage((int)damages + heroAtk);
            skillCooldown = 3;
        } else {
            setDamage(0);
            System.out.println("Your skill is on cooldown : " + skillCooldown);
            System.out.print("Please choose again : ");
        }
    }

    //defend reduce damage taken by 100% def
    //defend cooldown is 3 turns
    public void defend(int heroDef) {
        setDefense(heroDef);
        if(skillCooldown > 0) {
            skillCooldown -= 1;
        }
    }

    public int escape(int heroExp) {
        System.out.println("You choose to run away, you lose 50 experience!");
        heroExp = heroExp - 50;
        return heroExp;
    }
}