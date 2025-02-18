package com.bartodelini.pixel.ecs;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * An <i>Entity</i> is the base container for {@linkplain Component Components}.
 * It provides convenience methods for adding, removing, accessing, activating and deactivating {@code Components}.
 *
 * @author Bartolini
 * @version 1.3
 */

public final class Entity {

    private final UUID uuid = UUID.randomUUID();
    private final Map<Class<? extends Component>, List<Component>> componentMap = new HashMap<>();
    private final List<EntityObserver> entityObserverList = new CopyOnWriteArrayList<>();

    private String name;
    private Scene scene;
    private int layer;
    private String tag = "Untagged";
    private boolean active;

    /**
     * Allocates a new {@code Entity} object by passing in its name.
     *
     * @param name the name of this {@code Entity}.
     * @throws NullPointerException     if the specified name is {@code null}.
     * @throws IllegalArgumentException if the specified layer index as not in the valid range [0,31].
     */
    public Entity(String name, int layer) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (layer < 0 || layer > 31) {
            throw new IllegalArgumentException("layer index must be between 0 and 31");
        }
        this.layer = layer;
    }

    /**
     * Allocates a new {@code Entity} object by passing in its name, its layer and a varargs of
     * {@linkplain Component Components}.
     *
     * @param name       the name of this {@code Entity}.
     * @param layer      the layer of this {@code Entity}.
     * @param components the varargs containing the {@code Components} of this {@code Entity}.
     * @throws NullPointerException     if the specified name or any of the {@code Components} is {@code null}.
     * @throws IllegalArgumentException if the specified layer index as not in the valid range [0,31].
     */
    public Entity(String name, int layer, Component... components) {
        this(name, layer);
        addComponents(components);
    }

    /**
     * Allocates a new {@code Entity} object by passing in its name and varargs of {@linkplain Component Components}.
     * The default layer will be {@code 0}. This constructor has the same effect as
     * {@linkplain #Entity(String, int, Component...) Entity} {@code (name, 0, components)}.
     *
     * @param name       the name of this {@code Entity}.
     * @param components the varargs containing the {@code Components} of this {@code Entity}.
     * @throws NullPointerException if the specified name or any of the {@code Components} is {@code null}.
     */
    public Entity(String name, Component... components) {
        this(name, 0, components);
    }

    /**
     * Allocates a new {@code Entity} object by passing in its name. The default layer will be {@code 0}. This
     * constructor has the same effect as {@linkplain #Entity(String, int) Entity} {@code (name, 0)}.
     *
     * @param name the name of this {@code Entity}.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public Entity(String name) {
        this(name, 0);
    }

    /**
     * Allocates a new {@code Entity} object by passing in a varargs of {@linkplain Component Components}. The default
     * name will be "Entity" and the default layer will be {@code 0}. This constructor has the same effect as
     * {@linkplain #Entity(String, int, Component...) Entity} {@code ("Entity", 0, components)}.
     *
     * @param components the {@code Components} used to instantiate this {@code Entity}.
     * @throws NullPointerException if any of the {@code Components} specified in the varargs is {@code null}.
     */
    public Entity(Component... components) {
        this("Entity", 0, components);
    }

    /**
     * Allocates a new {@code Entity}. The default name will be "Entity" and the default layer will be {@code 0}. This
     * constructor has the same effect as {@linkplain #Entity(String, int) Entity} {@code ("Entity", 0)}.
     */
    public Entity() {
        this("Entity", 0);
    }

    /**
     * Returns the {@linkplain UUID} of this {@code Entity}.
     *
     * @return the {@code UUID} of this {@code Entity}.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Returns whether this {@code Entity} contains all the {@linkplain Component Components} of types specified in the
     * varargs of {@linkplain Class Classes}.
     *
     * @param componentClasses the varargs of {@code Classes} encapsulating the types of {@code Components} whose
     *                         presence is to be checked.
     * @return {@code true} if this {@code Entity} contains {@code Components} of all specified {@code Class} types;
     * {@code false} otherwise.
     * @throws NullPointerException if any of the {@code Class} types specified in the varargs is {@code null}.
     */
    @SafeVarargs
    public final boolean hasComponents(Class<? extends Component>... componentClasses) {
        for (Class<? extends Component> clazz : componentClasses) {
            if (!hasComponent(clazz)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether this {@code Entity} contains all the {@linkplain Component Components} of types specified in the
     * {@linkplain Iterable}.
     *
     * @param componentClasses the {@code Iterable} of {@code Classes} encapsulating the types of {@code Components}
     *                         whose presence is to be checked.
     * @return {@code true} if this {@code Entity} contains {@code Components} of all specified {@code Class} types;
     * {@code false} otherwise.
     * @throws NullPointerException if any of the {@code Class} types specified in the {@code Iterable} is {@code null}.
     */
    public boolean hasComponents(Iterable<Class<? extends Component>> componentClasses) {
        for (Class<? extends Component> clazz : componentClasses) {
            if (!hasComponent(clazz)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether this {@code Entity} contains a {@linkplain Component} of the specified {@linkplain Class} type.
     *
     * @param componentClass the {@code Class} type of the {@code Component} whose presence is to be checked.
     * @return {@code true} if this {@code Entity} contains a {@code Component} with the specified {@code Class} type;
     * {@code false} otherwise.
     * @throws NullPointerException if the specified {@code Class} type is {@code null}.
     */
    public boolean hasComponent(Class<? extends Component> componentClass) {
        Objects.requireNonNull(componentClass, "componentClass must not be null");
        return componentMap.containsKey(componentClass);
    }

    /**
     * Adds all {@linkplain Component Components} passed in as varargs to this {@code Entity}. Finally, notifies all
     * {@linkplain EntityObserver EntityObservers} of this {@code Entity} about the change.
     *
     * @param components the varargs of {@code Components} to be added to this {@code Entity}.
     * @throws NullPointerException  if one of the {@code Components} is {@code null}.
     * @throws IllegalStateException if one of the {@code Components} already has an owner.
     */
    public void addComponents(Component... components) {
        for (Component component : components) {
            addComponent(component);
        }
    }

    /**
     * Adds a {@linkplain Component} to this {@code Entity}. More formally adds the passed in {@code Component} to the
     * component map of this {@code Entity} with the keys being all superclasses of the passed in {@code Component} up
     * to {@code Component} class (exclusively) and the class of the passed in {@code Component} itself, sets the owner
     * of the {@code Component} to be this {@code Entity} and activates the {@code Component} if this {@code Entity}
     * itself is active. Finally, notifies all {@linkplain EntityObserver EntityObservers} of this {@code Entity} about
     * the change.
     *
     * @param component the {@code Component} to be added to this {@code Entity}.
     * @throws NullPointerException  if the specified {@code Component} is {@code null}.
     * @throws IllegalStateException if the specified {@code Component} already has an owner.
     */
    @SuppressWarnings("unchecked")
    public void addComponent(Component component) {
        Objects.requireNonNull(component, "component must not be null");
        if (hasComponent(component.getClass())) {
            return;
        }
        try {
            component.setOwner(this);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("A Component with an owner cannot be added");
        }

        // Add all superclasses up to Component (exclusively) as keys.
        Class<? extends Component> clazz = component.getClass();
        while (!clazz.equals(Component.class)) { // Stop at the Component class.
            componentMap.computeIfAbsent(clazz, k -> new ArrayList<>()).add(component);
            clazz = (Class<? extends Component>) clazz.getSuperclass();
        }

        // Activate the Component if this Entity is active.
        if (isActive()) {
            component.activate();
        }

        // Call the appropriate method on all component observers.
        entityObserverList.forEach(entityObserver -> entityObserver.componentAdded(this, component));
    }

    /**
     * Removes all {@linkplain Component Components} passed in as varargs from this {@code Entity}. Finally, notifies
     * all {@linkplain EntityObserver EntityObservers} of this {@code Entity} about the change.
     *
     * @param components the varargs of {@code Components} to be removed from this {@code Entity}.
     * @throws NullPointerException  if one of the {@code Components} is {@code null}.
     * @throws IllegalStateException if one of the {@code Components} is not attached to this {@code Entity}.
     */
    public void removeComponents(Component... components) {
        for (Component component : components) {
            removeComponent(component);
        }
    }

    /**
     * Removes the passed in {@linkplain Component} from this {@code Entity}. Finally, notifies all
     * {@linkplain EntityObserver EntityObservers} of this {@code Entity} about the change.
     *
     * @param component the {@code Component} to be removed from this {@code Entity}.
     * @throws NullPointerException  if the specified {@code Component} is {@code null}.
     * @throws IllegalStateException if the specified {@code Component} is not attached to this {@code Entity}.
     */
    @SuppressWarnings("unchecked")
    public void removeComponent(Component component) {
        Objects.requireNonNull(component, "component must not be null");
        if (component.getOwner() != this) {
            throw new IllegalStateException("component is not attached to this entity");
        }

        component.deactivate();

        Class<? extends Component> clazz = component.getClass();
        while (!clazz.equals(Component.class)) {
            List<Component> componentSet = componentMap.get(clazz);

            if (componentSet != null) {
                // Remove the component from the list of components.
                componentSet.remove(component);

                if (componentSet.isEmpty()) {
                    // Remove the empty component list from the component map.
                    componentMap.remove(clazz);
                }
            }
            clazz = (Class<? extends Component>) clazz.getSuperclass();
        }

        // Call the appropriate method on all component observers.
        entityObserverList.forEach(entityObserver -> entityObserver.componentRemoved(this, component));
    }

    /**
     * Returns the {@linkplain Component} whose type is encapsulated by the passed in {@linkplain Class}.
     * For the sake of convenience the returned {@code Component} is cast to the type encapsulated by the specified
     * {@code Class}.
     *
     * @param componentClass the {@code Class} encapsulating the desired {@code Component} type.
     * @param <T>            the desired {@code Component} type.
     * @return the {@code Component} whose type is encapsulated by the passed {@code Class}, or {@code null}
     * if no such {@code Component} is present in this {@code Entity}.
     * @throws NullPointerException if the specified {@code Class} encapsulating the {@code Component} is {@code null}.
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        Objects.requireNonNull(componentClass, "componentClass must not be null");
        if (componentMap.containsKey(componentClass)) {
            return componentClass.cast(componentMap.get(componentClass).get(0));
        }
        return null;
    }

    /**
     * Returns a list of {@linkplain Component Components} whose type is encapsulated by the specified
     * {@linkplain Class}. For the sake of convenience the returned list of {@code Components} is cast to the specified
     * {@code Class} type.
     *
     * @param componentClass the {@code Class} type of desired {@code Components}.
     * @param <T>            the desired {@code Component} type.
     * @return a list of {@code Components} of the specified {@code Class} type, or {@code null}
     * if no such {@code Component} is present in this {@code Entity}.
     * @throws NullPointerException if the specified {@code Class} type of {@code Components} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponents(Class<T> componentClass) {
        Objects.requireNonNull(componentClass, "clazz must not be null");
        return (List<T>) componentMap.get(componentClass);
    }

    /**
     * Returns an unmodifiable collection of all {@linkplain Component Components} present in this {@code Entity}.
     *
     * @return an unmodifiable collection of all {@code Components} present in this {@code Entity}.
     */
    public Set<Component> getAllComponents() {
        return componentMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    /**
     * Adds an {@linkplain EntityObserver} to this {@code Entity}.
     *
     * @param entityObserver the {@code EntityObserver} to be added to this {@code Entity}.
     * @throws NullPointerException if the specified {@code EntityObserver} is {@code null}.
     */
    public void addEntityObserver(EntityObserver entityObserver) {
        Objects.requireNonNull(entityObserver, "entityObserver must not be null");
        if (!entityObserverList.contains(entityObserver)) {
            entityObserverList.add(entityObserver);
        }
    }

    /**
     * Removes an {@linkplain EntityObserver} from this {@code Entity}.
     *
     * @param entityObserver the {@code EntityObserver} to be removed from this {@code Entity}.
     * @throws NullPointerException if the specified {@code EntityObserver} is {@code null}.
     */
    public void removeEntityObserver(EntityObserver entityObserver) {
        Objects.requireNonNull(entityObserver, "entityObserver must not be null");
        entityObserverList.remove(entityObserver);
    }

    /**
     * Returns the name of this {@code Entity}.
     *
     * @return the name of this {@code Entity}.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this {@code Entity} and notifies all {@linkplain EntityObserver EntityObservers} of this
     * {@code Entity} about the change.
     *
     * @param name the name of this {@code Entity}.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public void setName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (this.name != null && this.name.equals(name)) {
            return;
        }
        String oldName = this.name;
        this.name = name;
        entityObserverList.forEach(entityObserver -> entityObserver.nameChanged(this, oldName, name));
    }

    /**
     * Returns the layer this {@code Entity} is present in.
     *
     * @return the {@code Layer} this {@code Entity} is present in.
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Sets the layer for this {@code Entity} and notifies all {@linkplain EntityObserver EntityObservers} of this
     * {@code Entity} about the change.
     *
     * @param layer the layer for this {@code Entity}.
     * @throws IllegalArgumentException if the specified layer index as not in the valid range [0,31].
     */
    public void setLayer(int layer) {
        if (layer < 0 || layer > 31) {
            throw new IllegalArgumentException("layer index must be between 0 and 31");
        }
        if (this.layer == layer) {
            return;
        }
        int oldLayer = this.layer;
        this.layer = layer;
        entityObserverList.forEach(entityObserver -> entityObserver.layerChanged(this, oldLayer, layer));
    }

    /**
     * Returns the {@linkplain Scene} this {@code Entity} is present in.
     *
     * @return the {@code Scene} this {@code Entity} is present in.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Sets the {@linkplain Scene} of this {@code Entity} and notifies all {@linkplain EntityObserver EntityObservers}
     * of this {@code Entity} about the change.
     *
     * @param scene the new {@code Scene} for this {@code Entity}.
     * @throws IllegalStateException if this {@code Entity} is active.
     */
    public void setScene(Scene scene) {
        if (isActive()) {
            throw new IllegalStateException("cannot set the Scene of an active Entity");
        }
        if (this.scene != null && this.scene.equals(scene)) {
            return;
        }
        Scene oldScene = this.scene;
        this.scene = scene;
        entityObserverList.forEach(entityObserver -> entityObserver.sceneChanged(this, oldScene, scene));
    }

    /**
     * Returns the tag of this {@code Entity}.
     *
     * @return the tag of this {@code Entity}.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the tag of this {@code Entity} and notifies all {@linkplain EntityObserver EntityObservers} of this
     * {@code Entity} about the change.
     *
     * @param tag the new tag for this {@code Entity}.
     * @throws NullPointerException if the specified tag is {@code null}.
     */
    public void setTag(String tag) {
        Objects.requireNonNull(tag, "tag must not be null");
        if (this.tag.equals(tag)) {
            return;
        }
        String oldTag = this.tag;
        this.tag = tag;
        entityObserverList.forEach(entityObserver -> entityObserver.tagChanged(this, oldTag, tag));
    }

    /**
     * Activates this {@code Entity} and notifies all {@linkplain EntityObserver EntityObservers} of this
     * {@code Entity}
     * about the change. More formally activates this {@code Entity} and activates all its inactive
     * {@linkplain Component Components}.
     *
     * @throws IllegalStateException if this {@code Entity} is already active, or if the {@linkplain Scene} of this
     *                               {@code Entity} is not set.
     */
    public void activate() {
        if (isActive()) {
            throw new IllegalStateException("Entity already active");
        }
        if (getScene() == null) {
            throw new IllegalStateException("cannot activate an entity without an attached Scene");
        }
        active = true;

        // Activate all inactive Components.
        getAllComponents().stream().filter(component -> !component.isActive()).forEach(Component::activate);
        entityObserverList.forEach(entityObserver -> entityObserver.entityActivated(this));
    }

    /**
     * Deactivates this {@code Entity} and notifies all {@linkplain EntityObserver EntityObservers} of this
     * {@code Entity} about the change. More formally deactivates all active {@linkplain Component Components} present
     * in this {@code Entity} and deactivates this {@code Entity} at the end.
     *
     * @throws IllegalStateException if this {@code Entity} is already inactive.
     */
    public void deactivate() {
        if (!isActive()) {
            throw new IllegalStateException("Entity already inactive");
        }
        active = false;

        // Deactivate all active Components.
        getAllComponents().stream().filter(Component::isActive).forEach(Component::deactivate);
        entityObserverList.forEach(entityObserver -> entityObserver.entityDeactivated(this));
    }

    /**
     * Returns whether this {@code Entity} is active.
     *
     * @return {@code true} if this {@code Entity} is active; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        return getUUID().equals(entity.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUUID());
    }
}