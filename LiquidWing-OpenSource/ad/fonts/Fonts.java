/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package ad.fonts;

import com.google.gson.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.font.FontDetails;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer2;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts extends MinecraftInstance {

    @FontDetails(fontName = "Minecraft Font")
    public static final IFontRenderer minecraftFont = mc.getFontRendererObj();
    private static final HashMap<FontInfo, IFontRenderer> CUSTOM_FONT_RENDERERS = new HashMap<>();

    @FontDetails(fontName = "MiSans", fontSize = 18)
    public static IFontRenderer font18;
    @FontDetails(fontName = "MiSans", fontSize = 20)
    public static IFontRenderer font20;
    @FontDetails(fontName = "MiSans", fontSize = 30)
    public static IFontRenderer font30;
    @FontDetails(fontName = "MiSans", fontSize = 25)
    public static IFontRenderer font25;
    @FontDetails(fontName = "MiSans", fontSize = 35)
    public static IFontRenderer font35;
    @FontDetails(fontName = "MiSans", fontSize = 40)
    public static IFontRenderer font40;
    @FontDetails(fontName = "MiSans", fontSize = 128)
    public static IFontRenderer font128;

    public static void loadFonts() throws IOException {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts(1/5)...");

        //downloadFonts();
        
        
        font18 = classProvider.wrapFontRenderer(new GameFontRenderer2(getFont("misans.ttf", 18)));

        font20 = classProvider.wrapFontRenderer(new GameFontRenderer2(getFont("misans.ttf", 20)));
        font25 = classProvider.wrapFontRenderer(new GameFontRenderer2(getFont("misans.ttf", 25)));
        font30 = classProvider.wrapFontRenderer(new GameFontRenderer2(getFont("misans.ttf", 30)));
        font35 = classProvider.wrapFontRenderer(new GameFontRenderer2(getFont("misans.ttf", 35)));
        font40 = classProvider.wrapFontRenderer(new GameFontRenderer2(getFont("misans.ttf", 40)));

        font128 = classProvider.wrapFontRenderer(new GameFontRenderer2(getFont("misans.ttf", 128)));

        


        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.fontsDir, "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    Font font = getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt());

                    CUSTOM_FONT_RENDERERS.put(new FontInfo(font), classProvider.wrapFontRenderer(new GameFontRenderer2(font)));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception ignored) {

        }

        ClientUtils.getLogger().info("Loading Font(2/5)...");
        

        ClientUtils.getLogger().info("Loading Font(3/5)...");
        

        ClientUtils.getLogger().info("Loading Font(4/5)...");
        

        ClientUtils.getLogger().info("Loading Font(5/5)...");
        
        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void downloadFonts() throws IOException {
        final File outputFile = new File(LiquidBounce.fileManager.fontsDir, "roboto.zip");

        if (!outputFile.exists()) {
            ClientUtils.getLogger().info("Downloading fonts...");
            HttpUtils.download( "http://dl.liquidwing.cc/font.zip", outputFile);
            ClientUtils.getLogger().info("Extract fonts...");
            extractZip(outputFile.getPath(), LiquidBounce.fileManager.fontsDir.getPath());
        }
    }

    public static List<IFontRenderer> getFonts() {
        final List<IFontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof IFontRenderer) fonts.add((IFontRenderer) fontObj);
            } catch (final IllegalAccessException e) {

            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS.values());

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = Files.newInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName).toPath());
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final Exception e) {

            return new Font("default", Font.PLAIN, size);
        }
    }

    private static Font getCom(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(LiquidBounce.fileManager.fontsDir + "comfortaa.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }



    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if (!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return name;
        }

        public int getFontSize() {
            return fontSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FontInfo fontInfo = (FontInfo) o;

            if (fontSize != fontInfo.fontSize) return false;
            return Objects.equals(name, fontInfo.name);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + fontSize;
            return result;
        }
    }

}