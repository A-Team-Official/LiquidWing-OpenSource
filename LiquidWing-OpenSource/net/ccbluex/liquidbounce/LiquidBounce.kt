package net.ccbluex.liquidbounce

import ad.HytDisabler
import ad.NoLagBack
import ad.gui.MusicManager
import ad.modules.AutoLeave
import ad.modules.JumpCircle2
import ad.modules.PlayerSize
import ad.sb.Arraylist
import ad.sb.novoline.module.Ambience
import ad.sb.novoline.module.ChatTranslator
import ad.sb.novoline.module.TianKengHelper
import ad.sb.novoline.ui.GuiMainMenu
import ad.utils.Color.modules.CustomUI
import ad.utils.TipSoundManager
import net.ccbluex.liquidbounce.api.Wrapper
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.clickgui.ClickGUI
import net.ccbluex.liquidbounce.clickgui.ConfigSettingsCommand
import net.ccbluex.liquidbounce.clickgui.NewGUI
import net.ccbluex.liquidbounce.event.ClientShutdownEvent
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.command.CommandManager
import net.ccbluex.liquidbounce.features.command.ConfigCommand
import net.ccbluex.liquidbounce.features.command.commands.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.module.modules.client.UIEffects
import net.ccbluex.liquidbounce.features.module.modules.color.ColorMixer
import net.ccbluex.liquidbounce.features.module.modules.combat.*
import net.ccbluex.liquidbounce.features.module.modules.exploit.*
import net.ccbluex.liquidbounce.features.module.modules.gui.HudDesigner
import net.ccbluex.liquidbounce.features.module.modules.gui.TargetList
import net.ccbluex.liquidbounce.features.module.modules.hyt.*
import net.ccbluex.liquidbounce.features.module.modules.misc.*
import net.ccbluex.liquidbounce.features.module.modules.movement.*
import net.ccbluex.liquidbounce.features.module.modules.player.*
import net.ccbluex.liquidbounce.features.module.modules.render.*
import net.ccbluex.liquidbounce.features.module.modules.tomk.SuperKnockback
import net.ccbluex.liquidbounce.features.module.modules.world.*
import net.ccbluex.liquidbounce.features.special.AntiForge
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.management.CombatManager
import net.ccbluex.liquidbounce.management.MemoryManager
import net.ccbluex.liquidbounce.script.ScriptManager
import net.ccbluex.liquidbounce.script.remapper.Remapper.loadSrg
import net.ccbluex.liquidbounce.ui.cape.GuiCapeManager
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui
import net.ccbluex.liquidbounce.ui.client.hud.HUD
import net.ccbluex.liquidbounce.ui.client.hud.HUD.Companion.createDefault
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.*
import net.ccbluex.liquidbounce.utils.render.DrawArc
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.Display
import sun.misc.Unsafe
import verify.*
import java.awt.TrayIcon
import java.io.File
import java.io.IOException
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain
import java.text.SimpleDateFormat
import javax.swing.JOptionPane

object LiquidBounce: ClassFileTransformer {

    // Client information
    const val CLIENT_NAME = "LiquidWing"
    const val CLIENT_VERSION = "Reborn"
    const val CLIENT_CREATOR = "CCBlueX"
    const val CLIENT_CLOUD = "https://cloud.liquidbounce.net/LiquidBounce"

    var isStarting = false
    var height = -14f
    var aniHeight = -14f
    lateinit var username1: String

    // Managers
    lateinit var tipSoundManager: TipSoundManager
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var eventManager: EventManager
    lateinit var fileManager: FileManager
    lateinit var scriptManager: ScriptManager
    lateinit var combatManager: CombatManager
    lateinit var fontLoaders: FontLoaders
    var USERNAME = ""
    var QQ = ""
    var group = ""
    // HUD & ClickGUI
    lateinit var hud: HUD

    lateinit var clickGui: ClickGui
    var module: Module? = null
    var background: IResourceLocation? = null
    var mainMenuPrep = false
    lateinit var wrapper: Wrapper
    lateinit var Password: String
    var initclient = false
    fun setUsername(username: String) {
        this.USERNAME = username
    }
    var configchose = 0

    @JvmStatic
    var state: String = ""
    fun setQQ1(qq: String) {
        this.QQ = qq
    }
    lateinit var chooselanuage:Any

    override  fun transform(
        loader: ClassLoader?, className: String, classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain?, classfileBuffer: ByteArray?
    ): ByteArray? {
        // 检查是否要对当前类进行修改
        return if (isXBootAgent(className)) {
            // 禁用XBoot Agent
            null
        } else classfileBuffer
        // 不需要修改该类
    }

    private fun isXBootAgent(className: String): Boolean {
        // 检查类名是否为XBoot Agent
        return className == "com.alibaba.xhook.XHookImpl"
    }
    /**
     * Execute if client will be started
     */
    fun startClient() {
        System.out.println(".__   .__                .__     .___         .__                  \n" +
                "|  |  |__|  ______ __ __ |__|  __| _/__  _  __|__|  ____     ____  \n" +
                "|  |  |  | / ____/|  |  \\|  | / __ | \\ \\/ \\/ /|  | /    \\   / ___\\ \n" +
                "|  |__|  |< <_|  ||  |  /|  |/ /_/ |  \\     / |  ||   |  \\ / /_/  >\n" +
                "|____/|__| \\__   ||____/ |__|\\____ |   \\/\\_/  |__||___|  / \\___  / \n" +
                "              |__|                \\/                   \\/ /_____/");

        isStarting = true
        var ref: Unsafe? = null
        // fuck agent
        fun getInstrumentation(): Instrumentation? {
            return try {
                Instrumentation::class.java.getMethod("getInstrumentation").invoke(null) as Instrumentation?
            } catch (e: java.lang.Exception) {
                null
            }
        }

        val inst: Instrumentation? = getInstrumentation()

        // 检查 -javaagent
        if (inst != null) {
            System.exit(-1)
            Display.destroy()
            stopClient()

        }else {
            try {
                System.setProperty("java.lang.instrumentation", "false")

                Firstcheck.reg()
                if (!QQCheek.QQNumber.equals("{") && !QQCheek.QQNumber.contains("=")) {
                    System.setProperty("sun.misc.Unsafe", "MySQLDemo.class")
                    Firstcheck.Update4()
                    val clazz = Class.forName("sun.misc.Unsafe")
                    val theUnsafe = clazz.getDeclaredField("theUnsafe")
                    theUnsafe.isAccessible = true
                    ref = theUnsafe.get(null) as Unsafe
                    DrawArc.helper = "checkyou789007535678llloiygjiufgaserf"
                    state = "Wait for login"
                    eventManager = EventManager()

                    fileManager = FileManager()


                    Fonts.lfont()


                    val options = arrayOf<Any>("中文", "英文")
                    chooselanuage = JOptionPane.showInputDialog(
                        null, "请选择加载语言", "语言", JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]
                    )
                    if (chooselanuage.equals("中文")) {
                        CustomUI.Chinese.set(true)
                    } else {
                        CustomUI.Chinese.set(false)
                    }

                    Display.setTitle("LiquidWing | Not logged in")
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "QQ或Tim未启动,或多开QQ账户",
                        "失败",
                        JOptionPane.ERROR_MESSAGE
                    )
                    Firstcheck.dereg()
                    System.exit(1)
                }


            } catch (e: ClassNotFoundException) {
                System.exit(-1)
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                System.exit(-1)
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                System.exit(-1)
                e.printStackTrace()
            }
        }
    }

    fun stopClient() {
        state = "Logging in"
        var time = 0

        if (Firstcheck.X2name().equals(EncrypyUtil.encode(Firstcheck.getHWID()+QQCheek.QQNumber))) {

            MySQLDemo.getname = USERNAME
            if (MySQLDemo.getname != null) {
                MySQLDemo.UpdateHwid(MySQLDemo.getname)
                if (USERNAME.equals(MySQLDemo.X2name(MySQLDemo.getname))) {
                    if (Password.equals(MySQLDemo.X2pass(MySQLDemo.getname))) {
                        try {
                            if (MySQLDemo.X2Black(MySQLDemo.getname).equals("NO")) {
                                MySQLDemo.X2QQ(MySQLDemo.getname)
                                if (MySQLDemo.X2Hwid(MySQLDemo.getname) == MySQLDemo.getHWID1()) {
                                    if (MySQLDemo.X2sate(MySQLDemo.getname) == "YES") {
                                        MySQLDemo.Update9()
                                        //check version
                                        C0347SystemUtils.displayTray(
                                            "Successful",
                                            "登陆成功",
                                            TrayIcon.MessageType.INFO
                                        )
                                        println("OK")
                                        time = MySQLDemo.X2time(MySQLDemo.getname)
                                        if (time < 1) {
                                            C0347SystemUtils.displayTray(
                                                "卡密",
                                                "卡密剩余时间不到1天请你及时充值",
                                                TrayIcon.MessageType.WARNING
                                            )
                                            JOptionPane.showMessageDialog(
                                                null,
                                                "卡密剩余时间不到1天请你及时充值",
                                                "卡密",
                                                JOptionPane.WARNING_MESSAGE
                                            )
                                        } else {
                                            C0347SystemUtils.displayTray(
                                                "Hello",
                                                "User " + USERNAME + " You card residue " + time + " day",
                                                TrayIcon.MessageType.INFO
                                            )
                                        }
                                        val sdf = SimpleDateFormat(" yyyy-MM-dd HH:mm:ss")
                                        val time2 = (sdf.format(System.currentTimeMillis()))
                                        WbxMain.cheakuser()
                                        username1 = MySQLDemo.getname!!
                                        MySQLDemo.Update11()
                                        MySQLDemo.Update11()
                                        MySQLDemo.Update5(username1, time2)
                                        MySQLDemo.Update7(username1)
                                        MySQLDemo.Update4("Null", "127.0.0.1")

                                        DrawArc.helper = "checkyou789007535678llloiygjiufgaserf"
                                        ClientUtils.getLogger()
                                            .info("Starting $CLIENT_NAME b$CLIENT_VERSION, by $CLIENT_CREATOR")
                                        //TODO:HERE
                                        // Create file manager
                                        val groupcheck = WbxMain.cheakuser()
                                        val start = System.currentTimeMillis()

                                        if (groupcheck == 2) {
                                            C0347SystemUtils.displayTray(
                                                "Notification",
                                                "Hello Dev " + USERNAME,
                                                TrayIcon.MessageType.INFO
                                            )
                                            group = "Dev"
                                        } else if (groupcheck == 3) {
                                            C0347SystemUtils.displayTray(
                                                "Notification",
                                                "Hello Tester " + USERNAME,
                                                TrayIcon.MessageType.INFO
                                            )
                                            group = "Tester"
                                        } else if (groupcheck == 1) {
                                            C0347SystemUtils.displayTray(
                                                "Notification",
                                                "Hello Contributor " + USERNAME,
                                                TrayIcon.MessageType.INFO
                                            )
                                            group = "Contributor"
                                        } else {
                                            group = "User"

                                        }

                                        Fonts.loadFonts()
                                        FontLoaders.initFonts()
                                        ad.fonts.Fonts.loadFonts()
                                        // Create combat manager
                                        combatManager = CombatManager()
                                        moduleManager = ModuleManager()
                                        commandManager = CommandManager()

                                        fontLoaders = FontLoaders()
                                        // Register listeners
                                        eventManager.registerListener(BlurEvent())
                                        eventManager.registerListener(RotationUtils())
                                        eventManager.registerListener(AntiForge())
                                        eventManager.registerListener(BungeeCordSpoof())
                                        eventManager.registerListener(InventoryUtils())
                                        eventManager.registerListener(combatManager)
                                        eventManager.registerListener(MemoryManager())
                                        eventManager.registerListener(Test())
                                        eventManager.registerListener(UpdateGinfo())
                                        eventManager.registerListener(MusicManager())
                                        // Set HUD
                                        hud = createDefault()
                                        fileManager.loadConfig(fileManager.hudConfig)
                                        GuiCapeManager.load()

                                        tipSoundManager = TipSoundManager()

                                        // Setup module manager and register modules
                                        // Remapper
                                        try {
                                            loadSrg()

                                            // ScriptManager
                                            scriptManager = ScriptManager()
                                            scriptManager.loadScripts()
                                            scriptManager.enableScripts()
                                        } catch (throwable: Throwable) {
                                            ClientUtils.getLogger().error("Failed to load scripts.", throwable)
                                        }
                                        configchose = Configchose.chooseconfig()

                                        // Register commands
                                        registerCommands()



                                        registerModules()

                                        // ClickGUI
                                        clickGui = ClickGui()
                                        // Disable Optifine fast render


                                        fileManager.loadConfigs(
                                            fileManager.accountsConfig,
                                            fileManager.friendsConfig, fileManager.xrayConfig,
                                            fileManager.valuesConfig
                                        )
                                        ClientUtils.disableFastRender()

                                        // Load generators
                                        GuiAltManager.loadGenerators()

                                        ClientUtils.getLogger()
                                            .info("Loaded client in " + (System.currentTimeMillis() - start) + " ms.")

                                        Display.setTitle("LiquidWing User: " + GuiLogin.username!!.text + " | Have a good time")
                                        Minecraft.getMinecraft().displayGuiScreen(GuiMainMenu())
                                        Firstcheck.dereg()

                                        isStarting = false


                                    } else {
                                        JOptionPane.showMessageDialog(null, "账户未激活", "错误", 0)
                                        state = "账户未激活"

                                    }

                                } else {
                                    val chose: Int
                                    val options = arrayOf("Yes", "No")
                                    val kami: String = JOptionPane.showInputDialog(
                                        null,
                                        "账户绑定的HWID与当前HWID不符,请输入激活时的卡密",
                                        "Error",
                                        JOptionPane.QUESTION_MESSAGE
                                    )
                                    if (kami == MySQLDemo.X2card()) {
                                        chose = JOptionPane.showOptionDialog(
                                            null,
                                            "成功验证,你要修改HWID吗?",
                                            "Update",
                                            JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE,
                                            null,
                                            options,
                                            options.get(0)
                                        )
                                        if (chose == 0) {
                                            MySQLDemo.ChangeHwid()
                                            JOptionPane.showMessageDialog(
                                                null,
                                                "修改成功",
                                                "成功",
                                                JOptionPane.INFORMATION_MESSAGE
                                            )
                                            stopClient()
                                        } else if (chose == 1) {
                                            JOptionPane.showMessageDialog(
                                                null,
                                                "修改取消",
                                                "失败",
                                                JOptionPane.ERROR_MESSAGE
                                            )
                                            stopClient()
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(
                                            null,
                                            "卡密错误请联系管理员",
                                            "失败",
                                            JOptionPane.ERROR_MESSAGE
                                        )
                                        state = "Error:0008"
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "账户被拉入黑名单", "错误", 0)
                                state = "账户被拉入黑名单"

                            }
                        } catch (e: IOException) {
                            JOptionPane.showMessageDialog(
                                null,
                                "未知错误",
                                "失败",
                                JOptionPane.ERROR_MESSAGE
                            )
                            state = "未知错误"

                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "密码错误", "错误", 0)
                        state = "密码错误"

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "账户错误", "错误", 0)
                    state = "账户错误"

                }
            }else{
                JOptionPane.showInputDialog("错误","账户不存在")
            }
        }else {
            Firstcheck.Update4()

            JOptionPane.showMessageDialog(
                null,
                "许可未激活,但已发送请求",
                "failed",
                JOptionPane.ERROR_MESSAGE
            )

        }

    }

    fun registerCommands() {
        val configchose = configchose

        commandManager.registerCommand(BindCommand())
        commandManager.registerCommand(VClipCommand())
        commandManager.registerCommand(HClipCommand())
        commandManager.registerCommand(HelpCommand())
        commandManager.registerCommand(SayCommand())
        commandManager.registerCommand(FriendCommand())
        commandManager.registerCommand(ServerInfoCommand())
        commandManager.registerCommand(ToggleCommand())
        commandManager.registerCommand(TargetCommand())
        commandManager.registerCommand(BindsCommand())
        commandManager.registerCommand(PingCommand())
        commandManager.registerCommand(RenameCommand())
        commandManager.registerCommand(ReloadCommand())
        commandManager.registerCommand(ScriptManagerCommand())
        commandManager.registerCommand(RemoteViewCommand())
        commandManager.registerCommand(PrefixCommand())
        commandManager.registerCommand(HideCommand())
        commandManager.registerCommand(ConfigCommand())
        commandManager.registerCommand(HudCommand())
        commandManager.registerCommand(ConfigCommand())
        if (configchose == 114514){
            commandManager.registerCommand(ConfigSettingsCommand)
        }else if (configchose == 980340){
            commandManager.registerCommand(ConfigSettingsCommand)
        }
    }
    fun registerModules() {
        ClientUtils.getLogger().info("[ModuleManager] Loading modules...")

        moduleManager.registerModules(
            PingSpoof::class.java,
            HytAntiVoid::class.java,
            TargetList::class.java,
            FreeCam::class.java,
            LegitBhop::class.java,
            Disabler2::class.java,
            Ambience::class.java,
            Trails::class.java,
            NoC0F::class.java,
            BetterFPS::class.java,
            BWSpeedMine::class.java,
            CancelC03::class.java,
            AsianHat::class.java,
            PlayerSize::class.java,
            Arraylist::class.java,
            NoLagBack::class.java,
            HytDisabler::class.java,
            NoC03::class.java,
            MusicPlayer::class.java,
            AutoArmor::class.java,
            AutoBow::class.java,
            AutoRunaway::class.java,
            AutoPot::class.java,
            AutoSoup::class.java,
            AutoWeapon::class.java,
            BowAimbot::class.java,
            Criticals::class.java,
            AutoL::class.java,
            KillAura::class.java,
            Trigger::class.java,
            Fly::class.java,
            ClickGUI::class.java,
            InventoryMove::class.java,
            SafeWalk::class.java,
            WallClimb::class.java,
            Strafe::class.java,
            StrafeFix::class.java,
            GrimVelocity::class.java,
            Sprint::class.java,
            Teams::class.java,
            NoRotateSet::class.java,
            AntiBot::class.java,
            ChestStealer::class.java,
            Scaffold::class.java,
            FPSHurtCam::class.java,
            CivBreak::class.java,
            Tower::class.java,
            FastPlace::class.java,
            ESP::class.java,
            HytNoHurt::class.java,
            HytSpeed::class.java,
            NoSlow::class.java,
            Velocity::class.java,
            Speed::class.java,
            NameTags::class.java,
            Disabler::class.java,
            FastUse::class.java,
            Teleport::class.java,
            Fullbright::class.java,
            ItemESP::class.java,
            NoClip::class.java,
            FastClimb::class.java,
            Step::class.java,
            AutoRespawn::class.java,
            AutoTool::class.java,
            NoWeb::class.java,
            Regen::class.java,
            NoFall::class.java,
            Blink::class.java,
            NameProtect::class.java,
            NoHurtCam::class.java,
            XRay::class.java,
            Timer::class.java,
            Aimbot::class.java,
            Eagle::class.java,
            HitBox::class.java,
            AntiCactus::class.java,
            ConsoleSpammer::class.java,
            LongJump::class.java,
            FastBow::class.java,
            AutoClicker::class.java,
            NoBob::class.java,
            NoFriends::class.java,
            Chams::class.java,
            Clip::class.java,
            ServerCrasher::class.java,
            NoFOV::class.java,
            FastStairs::class.java,
            TNTBlock::class.java,
            InventoryCleaner::class.java,
            TrueSight::class.java,
            AntiBlind::class.java,
            NoSwing::class.java,
            Breadcrumbs::class.java,
            AntiVoid::class.java,
            AbortBreaking::class.java,
            PotionSaver::class.java,
            CameraClip::class.java,
            NoPitchLimit::class.java,
            AirLadder::class.java,
            TeleportHit::class.java,
            BufferSpeed::class.java,
            SuperKnockback::class.java,
            ProphuntESP::class.java,
            Damage::class.java,
            KeepContainer::class.java,
            VehicleOneHit::class.java,
            Reach::class.java,
            Rotations::class.java,
            NoJumpDelay::class.java,
            AntiAFK::class.java,
            BlockESP::class.java,
            NewGUI::class.java,
            ComponentOnHover::class.java,
            ResourcePackSpoof::class.java,
            NoSlowBreak::class.java,
            PortalMenu::class.java,
            EnchantEffect::class.java,
            KeepChest::class.java,
            SpeedMine::class.java,
            AutoHead::class.java,
            Animations::class.java,
            TpAura::class.java,
            BlurSettings::class.java,
            CustomUI::class.java,
            LegitSpeed::class.java,
            AutoLeave::class.java,
            Gapple::class.java,
            JumpCircle::class.java,
            JumpCircle2::class.java,
            HudDesigner::class.java,
            CapeManager::class.java,
            AutoPlay::class.java,
            ChatTranslator::class.java,
            DMGParticle::class.java,
            TianKengHelper::class.java,
            FakeFPS::class.java,
            ColorMixer::class.java,
            AuraKeepSprint::class.java,
            BedFucker::class.java,
            HotbarSettings::class.java,
            Crosshair::class.java,
            ad.gui.ui.TargetStrafe::class.java,
            Kick::class.java,
            UIEffects::class.java,
            HytAntiBot::class.java,
            FakeName::class.java,
            ScaffoldHelper::class.java
        )
        moduleManager.registerModule(NoScoreboard)
        moduleManager.registerModule(Fucker)
        moduleManager.registerModule(ChestAura)
        moduleManager.registerModule(UIEffects)
        moduleManager.registerModule( net.ccbluex.liquidbounce.features.module.modules.render.HUD)

        ClientUtils.getLogger().info("[ModuleManager] Loaded ${moduleManager.modules.size} modules.")
    }

    /**
     * Execute if client will be stopped
     */
    fun initClient() {
        // Call client shutdownd
        eventManager.callEvent(ClientShutdownEvent())

        // Save all available configs
        GuiCapeManager.save()
        fileManager.saveAllConfigs()
        MySQLDemo.Update8()
        MySQLDemo.Update3()
        val sdf=   SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
        val time2  = (sdf.format(System.currentTimeMillis()));
        MySQLDemo.Update6(time2)
        MySQLDemo.dereg()
        val file = File(fileManager.dir, "friends.json")
        file.writeText("")
        file.writeText("[]")
    }
}
@EventTarget
private fun onUpdate(event: UpdateEvent) {
    EntityUtils.targetPlayer = TargetList.playerValue.get()
    EntityUtils.targetAnimals = TargetList.animalsValue.get()
    EntityUtils.targetMobs = TargetList.mobsValue.get()
    EntityUtils.targetInvisible = TargetList.invisibleValue.get()
    EntityUtils.targetDead = TargetList.deadValue.get()
}