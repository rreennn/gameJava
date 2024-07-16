import java.sql.*;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Game {
    Scanner inp = new Scanner(System.in);
    private ArrayList<Enemy> enemies;
    private Connection connection = null;

    public Game() {
        enemies = new ArrayList<>();
        try {
            connection = Connector.getConnector();
            if (connection != null) {
                Statement stmt = connection.createStatement();
                System.out.println("Connected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // level up
    public void checkLevel(Hero player) {
        if (player.getExp() >= 1000 && player.getLevel() == 1) {
            System.out.println("Level UP! You are strong enough to go deeper in the forest!");
            player.setScore(player.getScore() + 500);
            System.out.println("Your current score is " + player.getScore() + "!");
            player.setLevel(2);
            player.setExp(player.getExp() - 1000);
        } else if (player.getExp() >= 3000 && player.getLevel() == 2) {
            player.setLevel(3);
            System.out.println("Your current score is " + player.getScore() + "!");
            bossStage(player);
        }
    }

    // for random enemy
    public void pickEnemy(Hero player) {
        if (player.getLevel() == 1) {
            enemies.add(new Enemy("Slime", 2000, 2000, 220, 110));
            enemies.add(new Enemy("Skeleton", 1800, 1800, 300, 80));
            enemies.add(new Enemy("Goblin", 2200, 2200, 280, 100));
        } else if (player.getLevel() == 2) {
            enemies.add(new Enemy("Ogre", 14000, 14000, 680, 600));
            enemies.add(new Enemy("Witch", 9700, 9700, 990, 300));
            enemies.add(new Enemy("Bandit", 11000, 11000, 790, 800));
        }
    }

    public void finalizeScore(Hero player) {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        Connector.insert(player.getName(), player.getScore(), date);
        ArrayList<Leaderboard> leaderboards = Connector.scoreRecords();
        Leaderboard.showList(leaderboards);
    }

    public void firstEnding(Hero player) {
        System.out.println("The monster is too strong! There's no choice but keep fighting");
        System.out.println("Sadly, you can't keep up, you feel the pain in your whole body");
        System.out.println("You're badly injured, but it keeps on beating you, until you finally lost consiousness");
        System.out.println("After days, the villagers that waiting for you realize, you couldn't make it");
        System.out.println("They still remember you as a hero and pray for your soul");
        player.setScore(player.getScore() + 500);
        System.out.println("==================GAME OVER======================");
        System.out.println("Your final score : " + player.getScore());
        player.setCurrentHp(0);
        finalizeScore(player);
    }

    public void secondEnding(Hero player) {
        System.out.println("After a long fight, the monster finally can't stand anymore");
        System.out.println("It's already dead");
        System.out.println(
                "You run quickly to find the princess in the monster nest, she sighs in relieve when she looks at you!");
        System.out.println(
                "Both of you, walk through the forest again and after some days, you two finally reach the village.");
        System.out.println("Everyone rejoice and the king is overjoyed!");
        System.out.println("The king gives you a lot of gold and make you the captain of kingdom military.");
        System.out.println("=====================GAME OVER==========================");
        player.setScore(player.getScore() + 1000);
        System.out.println("Your final score : " + player.getScore());
        player.setCurrentHp(0);
        finalizeScore(player);
    }

    // battle part
    public void fight(Hero player, Enemy enemy) {
        System.out.println("You encounter a " + enemy.getName());
        System.out.println("Choose your act!");
        System.out.println("1. Basic Atk");
        System.out.println("2. Skill Atk");
        System.out.println("3. Defend");
        System.out.println("4. Run away!");
        System.out.print("Your choice : ");

        boolean pick = true;
        while (pick) {
            try {
                int playerAct = inp.nextInt();
                System.out.println();
                if (playerAct == 1) {
                    player.basicAtk(player.getAtk());
                    System.out.println("You attack! Deals " + player.getDamage() + " damage");
                    pick = false;
                } else if (playerAct == 2) {
                    player.skillAtk(player.getAtk());
                    if (player.getDamage() != 0) {
                        System.out.println("You use your special skill! Deals " + player.getDamage() + " damage");
                        pick = false;
                    } else {
                        pick = true;
                    }
                } else if (playerAct == 3) {
                    player.defend(player.getDef());
                    System.out.println("You defend! Damage taken reduced by " + player.getDefense());
                    pick = false;
                } else if (playerAct == 4) {
                    System.out.println("You choose to run away");
                    player.setRunAway(true);
                    pick = false;
                } else {
                    System.out.println("Wrong number, please choose from 1-4");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please choose number 1-4");
                inp.next();
            }
        }

        int enemyAct = (int) (Math.random() * 2);
        if (player.runAway == false) {
            if (player.getDefense() != 0) {
                System.out.println("Enemy attack! Deals " + enemy.getAtk());
                enemy.attack(enemy.getAtk());
            } else if (enemyAct == 0) {
                System.out.println("Enemy defend! Your damage reduced!");
                enemy.defend(enemy.getDef());
            } else {
                System.out.println("Enemy attack! Deals " + enemy.getAtk());
                enemy.attack(enemy.getAtk());
            }
        } else if (player.runAway == true) {
            enemy.attack(0);
            enemy.defend(0);
        }

        if (player.getDefense() >= enemy.getDamage()) {
            player.setCurrentHp(player.getCurrentHp());
        } else {
            player.setCurrentHp(player.getCurrentHp() - (enemy.getDamage() - player.getDefense()));
        }

        if (enemy.getDefense() >= player.getDamage()) {
            enemy.setHp(enemy.getHp());
        } else {
            enemy.setHp(enemy.getHp() - (player.getDamage() - enemy.getDefense()));
        }

        player.setDamage(0);
        player.setDefense(0);
        enemy.setDamage(0);
        enemy.setDefense(0);

        if (player.getCurrentHp() < 0) {
            player.setCurrentHp(0);
        }

        if (enemy.getHp() < 0) {
            enemy.setHp(0);
        }

        System.out.println("Your HP is " + player.getCurrentHp() + " left!");
        System.out.println("Enemy's HP is " + enemy.getHp() + " left!");
    }

    // randomize and got 1 : found a treasure
    public void foundTreasure(Hero player) {
        System.out.println("You found 3 treasures on your way! Pick one!");
        System.out.println("1. Increase Atk");
        System.out.println("2. Increase Def");
        System.out.println("3. Increase HP");

        boolean choose = true;
        while (choose) {
            try {
                int pick = inp.nextInt();
                if (player.getLevel() == 1) {
                    if (pick == 1) {
                        player.setAtk(player.getAtk() + 50);
                        choose = false;
                    } else if (pick == 2) {
                        player.setDef(player.getDef() + 30);
                        choose = false;
                    } else if (pick == 3) {
                        player.setMaxHp(player.getMaxHp() + 100);
                        player.setCurrentHp(player.getCurrentHp() + 50);
                        choose = false;
                    } else {
                        System.out.println("Please choose number 1-4");
                    }
                } else if (player.getLevel() == 2) {
                    if (pick == 1) {
                        player.setAtk(player.getAtk() + 150);
                        choose = false;
                    } else if (pick == 2) {
                        player.setDef(player.getDef() + 90);
                        choose = false;
                    } else if (pick == 3) {
                        player.setMaxHp(player.getMaxHp() + 300);
                        player.setCurrentHp(player.getCurrentHp() + 300);
                        choose = false;
                    } else {
                        System.out.println("Please choose number 1-4");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong input, please choose number 1-4");
                inp.next();
            }
        }

        player.setExp(player.getExp() + 50);
        checkLevel(player);
        reportStats(player);
    }

    public void bossStage(Hero player) {
        Boss boss = new Boss("Rakshasa", 35000, 35000, 2300, 3000);
        System.out.println(
                "You finally reach the deepest part of the forest. It didn't take a long time until you found it.");
        System.out.println("The monster that kidnapped the princess!");
        System.out.println("You prepared to fight!");
        System.out.println(
                player.getName() + " : Hey you monster! Free the princess, her father is concerned about her!");
        System.out.println(boss.getName() + " : AHAHAHAHA NEVER! OVER MY DEAD BODY! COME ON FIGHT ME!");
        System.out.println(
                "====================================================================================================");
        System.out.println("Fight " + boss.getName() + " and win");
        player.setCurrentHp(player.getMaxHp());
        boolean bossFight = true;
        while (bossFight) {
            System.out.println("Choose your act!");
            System.out.println("1. Basic Atk");
            System.out.println("2. Skill Atk");
            System.out.println("3. Defend");
            System.out.print("Your choice : ");

            boolean pick = true;
            while (pick) {
                try {
                    int playerAct = inp.nextInt();
                    if (playerAct == 1) {
                        player.basicAtk(player.getAtk());
                        System.out.println("You attack! Deals " + player.getDamage() + " damage");
                        pick = false;
                    } else if (playerAct == 2) {
                        player.skillAtk(player.getAtk());
                        if (player.getDamage() != 0) {
                            System.out.println("You use your special skill! Deals " + player.getDamage() + " damage");
                            pick = false;
                        } else {
                            pick = true;
                        }
                    } else if (playerAct == 3) {
                        player.defend(player.getDef());
                        System.out.println("You defend! Damage taken reduced by " + player.getDefense());
                        pick = false;
                    } else {
                        System.out.println("Wrong pick, pick again!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please choose from number 1-3");
                    inp.next();
                }
            }

            boolean bossTurn = true;
            while (bossTurn) {
                int bossAct = (int) (Math.random() * 3);
                if (player.getDefense() != 0) {
                    boss.attack(boss.getAtk());
                } else if (bossAct == 0) {
                    System.out.println(boss.getName() + " attack! Deals " + boss.getAtk());
                    boss.attack(boss.getAtk());
                    bossTurn = false;
                } else if (bossAct == 1) {
                    System.out.println(boss.getName() + " defend! Your damage reduced!");
                    boss.defend(boss.getDef());
                    bossTurn = false;
                } else if (bossAct == 2) {
                    boss.bossSkill(boss.getAtk());
                    if (boss.getDamage() != 0) {
                        System.out.println(
                                boss.getName() + " use its special skill! Deals " + boss.getDamage() + " damage!");
                        bossTurn = false;
                    } else {
                        bossTurn = true;
                    }
                }
            }

            if (player.getDefense() >= boss.getDamage()) {
                player.setCurrentHp(player.getCurrentHp());
            } else {
                player.setCurrentHp(player.getCurrentHp() - (boss.getDamage() - player.getDefense()));
            }

            if (boss.getDefense() >= player.getDamage()) {
                boss.setHp(boss.getHp());
            } else {
                boss.setHp(boss.getHp() - (player.getDamage() - boss.getDefense()));
            }

            player.setDamage(0);
            player.setDefense(0);
            boss.setDamage(0);
            boss.setDefense(0);

            if (player.getCurrentHp() < 0) {
                player.setCurrentHp(0);
            }

            if (boss.getHp() < 0) {
                boss.setHp(0);
            }

            System.out.println("Your HP is " + player.getCurrentHp() + " left!");
            System.out.println(boss.getName() + "boss's HP is " + boss.getHp() + " left!");

            if (boss.getHp() <= 0) {
                secondEnding(player);
                bossFight = false;
            } else if (player.getCurrentHp() <= 0) {
                firstEnding(player);
                bossFight = false;
            }
        }
    }

    public void reportStats(Hero player) {
        System.out.println(
                "==================================================================================");
        System.out.println("Here's your updated strength");
        System.out.println("Max Health Point : " + player.getMaxHp());
        System.out.println("Current Health Point : " + player.getCurrentHp());
        System.out.println("Attack : " + player.getAtk());
        System.out.println("Defend : " + player.getDef());
        System.out.println("Exp : " + player.getExp());
        System.out.println("Level : " + player.getLevel());
        System.out.println(
                "==================================================================================");
    }

    public void startGame() {
        System.out.println("=================================================================================");
        System.out.println("Welcome our Hero! How may us address you?");
        System.out.print("Enter your name : ");
        String name = inp.nextLine();
        Hero player = new Hero(name);
        player.setAtk(200);
        player.setMaxHp(3000);
        player.setDef(100);
        player.setCurrentHp(player.getMaxHp());
        System.out.println("With God Blessing, you're blessed with current strength");
        System.out.println("Health Point : " + player.getMaxHp());
        System.out.println("Attack : " + player.getAtk());
        System.out.println("Defend : " + player.getDef());
        System.out.println("=================================================================================");
        System.out.println(player.getName() + "! You're so strong, please help us to save the princess!");
        System.out.println("1 week ago, our beloved princess got kidnapped by strong monster in the forest!");
        System.out.println("Only you can beat that wicked monster! The king would gladly reward you.");
        System.out.println("We can only rely on you! We wish you all the best, Hero " + player.getName());
        System.out.println("==================================================================================");
        player.setExp(200);
        player.setLevel(1);
        player.setScore(0);

        // menu start
        while (player.isAlive()) {
            System.out.println(name + " : What should I do now?");
            System.out.println("1. Explore");
            System.out.println("2. Rest");

            boolean pick = true;
            while (pick) {
                try {
                    int choose = inp.nextInt();
                    switch (choose) {
                        case 1:
                            System.out.println("You choose to explore the forest.");
                            int randomExplore = (int) (Math.random() * 2);
                            if (randomExplore == 0) {
                                foundTreasure(player);
                            } else {
                                System.out.println("You encounter an enemy! Be careful!");
                                System.out.println(
                                        "==================================================================================");
                                int randomEnemy = (int) (Math.random() * 3);
                                boolean battle = true;
                                while (battle) {
                                    pickEnemy(player);
                                    if (randomEnemy == 0) {
                                        fight(player, enemies.get(randomEnemy));
                                    } else if (randomEnemy == 1) {
                                        fight(player, enemies.get(randomEnemy));
                                    } else if (randomEnemy == 2) {
                                        fight(player, enemies.get(randomEnemy));
                                    }
                                    System.out.println(
                                            "==================================================================================");
                                    // pls make battle stop
                                    if (enemies.get(randomEnemy).getHp() <= 0 && player.getCurrentHp() <= 0) {
                                        System.out.println(
                                                "You're keep fighting and fighting, you realize that you made a wrong move");
                                        System.out.println("But your pride as hero makes you keep going!");
                                        System.out
                                                .println(
                                                        "At the end, you barely win this battle, both of you and your enemy");
                                        System.out.println("Now you're too tired to continue your journey.");
                                        System.out.println("As you close your eyes, you fall into darkness");
                                        System.out.println("== GAME OVER ==");
                                        finalizeScore(player);
                                        battle = false;
                                    } else if (enemies.get(randomEnemy).getHp() <= 0) {
                                        player.setSkillCooldown(0);
                                        System.out.println("Enemy defeated");
                                        System.out.println("You become stronger!");
                                        player.setAtk(player.getAtk() + 100);
                                        player.setMaxHp(player.getMaxHp() + 500);
                                        player.setDef(player.getDef() + 30);
                                        player.setExp(player.getExp() + 100);
                                        player.setScore(player.getScore() + 150);
                                        checkLevel(player);
                                        reportStats(player);
                                        enemies.clear();
                                        battle = false;
                                    } else if (player.getCurrentHp() <= 0) {
                                        System.out.println("The enemy beat you up!");
                                        System.out.println("You badly injured and can't go on");
                                        System.out.println(
                                                "==================================================================================");
                                        System.out.println("== GAME OVER ==");
                                        finalizeScore(player);
                                        battle = false;
                                    } else if (player.runAway == true) {
                                        System.out.println("You decided to retreat, and it's wise choice");
                                        player.setSkillCooldown(0);
                                        player.setScore(player.getScore() - 50);
                                        player.setRunAway(false);
                                        checkLevel(player);
                                        reportStats(player);
                                        enemies.clear();
                                        battle = false;
                                    }
                                }
                            }
                            pick = false;
                            break;

                        case 2:
                            System.out.println(name + " : I'm gonna take a rest");
                            System.out.println("You take a rest and healed your HP!");
                            player.setScore(player.getScore() - 100);
                            player.setCurrentHp(player.getMaxHp());
                            reportStats(player);
                            pick = false;
                            break;
                        default:
                            System.out.println("Please pick number 1-2");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Wrong input, please choose number 1-2");
                    inp.next();
                }
            }

        }

    }
}
