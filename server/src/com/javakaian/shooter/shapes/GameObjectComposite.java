package com.javakaian.shooter.shapes;

import java.util.ArrayList;
import java.util.List;

public class GameObjectComposite implements GameObject {

    private GameObjectComposite parent;
    private List<GameObject> children = new ArrayList<>();

    public void add(GameObject obj) {
        if (obj instanceof GameObjectComposite composite)
            composite.setParent(this);

        children.add(obj);
    }

    public void remove(GameObject obj) {
        children.remove(obj);

        if (obj instanceof GameObjectComposite composite)
            composite.setParent(null);
    }

    public <T extends GameObject> List<T> getAll(Class<T> type) {
        List<T> result = new ArrayList<>();

        for (GameObject obj : children) {
            if (type.isInstance(obj))
                result.add(type.cast(obj));

            if (obj instanceof GameObjectComposite composite)
                result.addAll(composite.getAll(type));
        }
        return result;
    }

    public void setParent(GameObjectComposite p) {
        this.parent = p;
    }

    public GameObjectComposite getParent() {
        return parent;
    }

    @Override
    public void update(UpdateContext context) {
        for (GameObject obj : children) {
            obj.update(context);
        }
    }

    @Override
    public boolean isAlive() {
        for (GameObject obj : children) {
            if (obj.isAlive()) return true;
        }
        return false;
    }
}
