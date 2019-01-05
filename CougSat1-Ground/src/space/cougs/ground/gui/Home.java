package space.cougs.ground.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;

import space.cougs.ground.CougSatGround;
import space.cougs.ground.gui.modules.BodyLabel;
import space.cougs.ground.gui.modules.CISCheckBox;
import space.cougs.ground.gui.modules.CISPanel;
import space.cougs.ground.gui.modules.CISScrollPane;
import space.cougs.ground.gui.modules.CISTextArea;
import space.cougs.ground.gui.modules.CISTextField;
import space.cougs.ground.gui.modules.ImageModule;
import space.cougs.ground.gui.modules.TitleLabel;
import space.cougs.ground.gui.utils.CustomColors;
import space.cougs.ground.gui.utils.GridBagConstraintsWrapper;
import space.cougs.ground.utils.FileUtils;

public class Home extends CISPanel {
  private static final long serialVersionUID = 1L;

  private final CISPanel patchNotesPanel   = new CISPanel();
  private final CISTextArea patchNotesBody = new CISTextArea();
  private final CISScrollPane patchNotesScroll =
      new CISScrollPane(patchNotesBody);

  private final CISPanel aboutPanel   = new CISPanel();
  private final CISTextArea aboutBody = new CISTextArea(8, 30);

  private final CISPanel infoPanel    = new CISPanel();
  private final CISPanel optionsPanel = new CISPanel();

  private final CISPanel filesAndDirectories = new CISPanel();
  private final CISTextField homeDirectory   = new CISTextField();

  private final CISPanel groundStationInfo         = new CISPanel();
  private final CISTextField groundstationName     = new CISTextField();
  private final CISTextField latitude              = new CISTextField();
  private final CISTextField longitude             = new CISTextField();
  private final CISTextField gridLocator           = new CISTextField();
  private final CISTextField altitude              = new CISTextField();
  private final CISTextField rfRecieverDescription = new CISTextField();

  private final CISPanel decoderPanel = new CISPanel();
  private final CISCheckBox uploadToServer =
      new CISCheckBox("Upload to Server");
  private final CISCheckBox trackDoppler = new CISCheckBox("Track Doppler");
  private final CISCheckBox storePayload = new CISCheckBox("Store Payload");
  private final CISCheckBox swapIQ       = new CISCheckBox("Swap I/Q");
  private final CISCheckBox fixDroppedBits =
      new CISCheckBox("Fix Dropped Bits");

  private final CISPanel debugPanel       = new CISPanel();
  private final CISCheckBox enableLogging = new CISCheckBox("Enable Logging");
  private final CISCheckBox debugFrames   = new CISCheckBox("Debug Frames");
  private final CISCheckBox debugClock    = new CISCheckBox("Debug Clock");
  private final CISCheckBox debugAudio  = new CISCheckBox("Debug Missed Audio");
  private final CISCheckBox debugSignal = new CISCheckBox("Debug Find Signal");

  private final ImageModule logoPanel =
      new ImageModule(FileUtils.getImage("CISClubLogo-1000.png"));

  public Home() {
    super();

    layoutInfoPanel();
    layoutOptionPanel();

    this.setLayout(new GridLayout(0, 2, 10, 10));
    this.add(infoPanel);
    this.add(optionsPanel);
    this.setBackground(CustomColors.SECONDARY);
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    loadText();
  }

  private void layoutInfoPanel() {
    logoPanel.setLink("http://cougs.space");

    aboutBody.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    patchNotesBody.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    aboutPanel.setLayout(new BorderLayout());
    aboutPanel.add(new TitleLabel("About"), BorderLayout.PAGE_START);
    aboutPanel.add(aboutBody, BorderLayout.CENTER);

    patchNotesPanel.setLayout(new BorderLayout());
    patchNotesPanel.add(new TitleLabel("Patch Notes"), BorderLayout.PAGE_START);
    patchNotesPanel.add(patchNotesScroll, BorderLayout.CENTER);

    infoPanel.setLayout(new GridLayout(3, 0, 10, 10));
    infoPanel.setOpaque(false);
    infoPanel.add(logoPanel);
    infoPanel.add(aboutPanel);
    infoPanel.add(patchNotesPanel);
  }

  private void layoutOptionPanel() {
    GridBagConstraintsWrapper gbc = new GridBagConstraintsWrapper();
    gbc.setInsets(5, 5, 5, 5).setWeight(1.0, 1.0);

    filesAndDirectories.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    filesAndDirectories.setLayout(new BorderLayout());
    filesAndDirectories.add(
        new TitleLabel("Home Directory"), BorderLayout.PAGE_START);
    filesAndDirectories.add(homeDirectory, BorderLayout.CENTER);

    groundStationInfo.setLayout(new GridBagLayout());
    groundStationInfo.add(new TitleLabel("Ground Station Info"),
        gbc.setCommon(0, 0, 2, 1, 0.0, 0.0));
    groundStationInfo.add(new BodyLabel("GroundStation Name: "),
        gbc.setCommon(0, 1, 1, 1, 0.0, 1.0));
    groundStationInfo.add(new BodyLabel("Longitude: "), gbc.setXY(0, 2));
    groundStationInfo.add(new BodyLabel("Latitude: "), gbc.setXY(0, 3));
    groundStationInfo.add(new BodyLabel("Grid Locator: "), gbc.setXY(0, 4));
    groundStationInfo.add(new BodyLabel("Altitude (m): "), gbc.setXY(0, 5));
    groundStationInfo.add(
        new BodyLabel("RF Radio Description: "), gbc.setXY(0, 6));
    groundStationInfo.add(
        groundstationName, gbc.setXY(1, 1).setWeight(1.0, 1.0));
    groundStationInfo.add(longitude, gbc.setXY(1, 2));
    groundStationInfo.add(latitude, gbc.setXY(1, 3));
    groundStationInfo.add(gridLocator, gbc.setXY(1, 4));
    groundStationInfo.add(altitude, gbc.setXY(1, 5));
    groundStationInfo.add(rfRecieverDescription, gbc.setXY(1, 6));

    decoderPanel.setLayout(new GridBagLayout());
    decoderPanel.add(
        new TitleLabel("Decoder Options"), gbc.setXY(0, 0).setWeight(1.0, 0.0));
    decoderPanel.add(uploadToServer, gbc.setXY(0, 1).setWeight(1.0, 1.0));
    decoderPanel.add(trackDoppler, gbc.setXY(0, 2));
    decoderPanel.add(storePayload, gbc.setXY(0, 3));
    decoderPanel.add(swapIQ, gbc.setXY(0, 4));
    decoderPanel.add(fixDroppedBits, gbc.setXY(0, 5));

    debugPanel.setLayout(new GridBagLayout());
    debugPanel.add(
        new TitleLabel("Debug Options"), gbc.setXY(0, 0).setWeight(1.0, 0.0));
    debugPanel.add(enableLogging, gbc.setXY(0, 1).setWeight(1.0, 1.0));
    debugPanel.add(debugFrames, gbc.setXY(0, 2));
    debugPanel.add(debugClock, gbc.setXY(0, 3));
    debugPanel.add(debugAudio, gbc.setXY(0, 4));
    debugPanel.add(debugSignal, gbc.setXY(0, 5));

    optionsPanel.setLayout(new GridBagLayout());
    optionsPanel.setOpaque(false);
    optionsPanel.add(new TitleLabel("Options"),
        gbc.setCommon(0, 0, 2, 1, 1.0, 0.0).setInsets(0, 0, 5, 0));
    optionsPanel.add(filesAndDirectories,
        gbc.setCommon(0, 1, 2, 1, 1.0, 1.0).setInsets(5, 0, 5, 0));
    optionsPanel.add(groundStationInfo, gbc.setCommon(0, 2, 2, 1));
    optionsPanel.add(
        decoderPanel, gbc.setCommon(0, 3, 1, 1).setInsets(5, 0, 0, 5));
    optionsPanel.add(
        debugPanel, gbc.setCommon(1, 3, 1, 1).setInsets(5, 5, 0, 0));
  }

  private void loadText() {
    File aboutFile = new File("resources\\About.txt");
    if (!aboutFile.exists()) {
      System.out.printf(
          "About file does not exist: %s\n", aboutFile.getAbsolutePath());
    } else {
      try {
        BufferedReader fileIn = new BufferedReader(new FileReader(aboutFile));
        aboutBody.read(fileIn, null);
        fileIn.close();
      } catch (IOException e) {
        System.out.printf(
            "Failed to read about file: %s", aboutFile.getAbsolutePath());
        e.printStackTrace();
      }
    }
    File patchNotesFile = new File(
        "resources\\PatchNotes." + CougSatGround.getVersionnumber() + ".txt");
    if (!patchNotesFile.exists()) {
      System.out.printf("Patch notes file does not exist: %s\n",
          patchNotesFile.getAbsolutePath());
    } else {
      try {
        BufferedReader fileIn =
            new BufferedReader(new FileReader(patchNotesFile));
        patchNotesBody.read(fileIn, null);
        fileIn.close();
      } catch (IOException e) {
        System.out.printf(
            "Failed to read about file: %s", patchNotesFile.getAbsolutePath());
        e.printStackTrace();
      }
    }
    homeDirectory.setText(CougSatGround.getHomeDir().getAbsolutePath());
  }
}