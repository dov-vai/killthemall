package com.javakaian.shooter.command;

import com.javakaian.states.PlayState;

/**
 * Command to toggle attachment in PlayState
 * This is not undoable as it's a toggle action
 */
public class AttachmentToggleCommand implements InputCommand {
    private final PlayState playState;
    private final String attachmentSpec;

    public AttachmentToggleCommand(PlayState playState, String attachmentSpec) {
        this.playState = playState;
        this.attachmentSpec = attachmentSpec;
    }

    @Override
    public void execute() {
        playState.requestAttachmentChange(attachmentSpec);
    }

    @Override
    public void undo() {
        // Could toggle back, but it's simpler to just let user toggle again
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Toggle Attachment: " + attachmentSpec;
    }
}
