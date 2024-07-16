
public class Boss extends Enemy {
    int bossSkillCd;

    public Boss(String name, int maxHp, int hp, int atk, int def) {
        super(name, maxHp, hp, atk, def);
    }

    public void bossSkill(int bossAtk) {
        if(bossSkillCd == 0) {
            double damages = bossAtk * 0.7;
            setDamage((int)damages + bossAtk);
            bossSkillCd = 3;
        } else {
            setDamage(0);
        }
    }

}
