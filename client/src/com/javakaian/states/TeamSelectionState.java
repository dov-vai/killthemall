package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.input.TeamSelectionInput;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.Subsystems.TextAlignment;
import com.javakaian.shooter.utils.fonts.FontManager;

/**
 * State for selecting a team before joining the game.
 * Demonstrates Mediator pattern - selected team determines chat routing.
 */
public class TeamSelectionState extends State {
    
    private FontManager titleFontManager;
    private FontManager instructionFontManager;
    private BitmapFont titleFont;
    private BitmapFont instructionFont;
    private String selectedTeam;
    
    public TeamSelectionState(StateController sc) {
        super(sc);
        
        titleFontManager = new FontManager(40, Color.WHITE, "Warungasem.ttf", "TeamSelection-Title");
        titleFontManager.switchToVirtualProxy();
        titleFont = titleFontManager.getFontResource().getFont();
        
        instructionFontManager = new FontManager(20, Color.GRAY, "Warungasem.ttf", "TeamSelection-Instructions");
        instructionFontManager.switchToVirtualProxy();
        instructionFont = instructionFontManager.getFontResource().getFont();
        
        ip = new TeamSelectionInput(this);
        selectedTeam = null;
    }
    
    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        GameManagerFacade gm = GameManagerFacade.getInstance();
        
        sb.begin();
        
        // Title
        gm.renderText(sb, titleFont, "Select Your Team", TextAlignment.CENTER, 0f, 0.2f);
        
        // Instructions
        gm.renderText(sb, instructionFont, "Press 1 for RED Team", TextAlignment.CENTER, 0f, 0.4f);
        gm.renderText(sb, instructionFont, "Press 2 for BLUE Team", TextAlignment.CENTER, 0f, 0.45f);
        gm.renderText(sb, instructionFont, "Press 3 for GREEN Team", TextAlignment.CENTER, 0f, 0.5f);
        
        // Show selected team
        if (selectedTeam != null) {
            Color teamColor = getTeamColor(selectedTeam);
            BitmapFont selectedFont = GameManagerFacade.getInstance().generateBitmapFont(30, teamColor);
            gm.renderText(sb, selectedFont, 
                "Selected: " + selectedTeam + " Team", TextAlignment.CENTER, 0f, 0.65f);
            gm.renderText(sb, instructionFont, 
                "Press SPACE to join game", TextAlignment.CENTER, 0f, 0.75f);
        }
        
        sb.end();
    }
    
    @Override
    public void update(float deltaTime) {
        // Input handled by TeamSelectionInput
    }
    
    @Override
    public void dispose() {
        titleFontManager.dispose();
        instructionFontManager.dispose();
    }
    
    public void selectTeam(String team) {
        this.selectedTeam = team;
    }
    
    public String getSelectedTeam() {
        return selectedTeam;
    }
    
    public void joinGame() {
        if (selectedTeam != null) {
            PlayState playState = (PlayState) sc.getStateMap().get(StateEnum.PLAY_STATE.ordinal());
            
            // Create PlayState if it doesn't exist
            if (playState == null) {
                playState = new PlayState(sc);
                sc.getStateMap().put(StateEnum.PLAY_STATE.ordinal(), playState);
            }
            
            playState.setSelectedTeam(selectedTeam);
            sc.setState(StateEnum.PLAY_STATE);
        }
    }
    
    public void backToMenu() {
        selectedTeam = null;
        sc.setState(StateEnum.MENU_STATE);
    }
    
    private Color getTeamColor(String team) {
        return switch (team) {
            case "RED" -> Color.RED;
            case "BLUE" -> Color.BLUE;
            case "GREEN" -> Color.GREEN;
            default -> Color.WHITE;
        };
    }
}
