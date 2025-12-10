package com.javakaian.shooter.memento;

/**
 * Narrow Interface for Memento Pattern
 * This interface is used by the Caretaker and other objects.
 * It provides no methods to access the state - it's just a marker
 * that allows passing mementos around without exposing internal data.
 */
public interface IMemento {
    // Intentionally empty - narrow interface
    // Caretaker can store and pass mementos but cannot access their state
}