package com.bartodelini.pixel.ecs;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A <i>Scene</i> is the main container for {@linkplain Entity Entities}.
 *
 * @author Bartolini
 * @version 1.4
 */
public class Scene {

    // General containers for Entities and Components
    private final Set<Entity> entitySet = new LinkedHashSet<>();
    private final Map<Class<? extends Component>, Set<Component>> componentMap = new HashMap<>();
    private final Map<Set<Class<? extends Component>>, Set<Entity>> entityMap = new HashMap<>();

    // Unmodifiable views on general containers for Entities and Components
    private Set<Entity> unmodifiableEntitySet = new LinkedHashSet<>();
    private final Map<Class<? extends Component>, Set<Component>> unmodifiableComponentMap = new HashMap<>();
    private final Map<Set<Class<? extends Component>>, Set<Entity>> unmodifiableEntityMap = new HashMap<>();

    // Containers for Entities and Components grouped by corresponding layers
    private final List<Set<Entity>> entitySetList = new ArrayList<>(32);
    private final Map<Class<? extends Component>, List<Set<Component>>> layeredComponentMap = new HashMap<>();
    private final Map<Set<Class<? extends Component>>, List<Set<Entity>>> layeredEntityMap = new HashMap<>();

    // Unmodifiable views on containers for Entities and Components grouped by corresponding layers
    private List<Set<Entity>> unmodifiableEntitySetList = new ArrayList<>(32);
    private final Map<Class<? extends Component>, List<Set<Component>>> unmodifiableLayeredComponentMap = new HashMap<>();
    private final Map<Set<Class<? extends Component>>, List<Set<Entity>>> unmodifiableLayeredEntityMap = new HashMap<>();

    private final List<SceneObserver> sceneObserverList = new CopyOnWriteArrayList<>();

    /**
     * Allocates a new {@code Scene} object.
     */
    public Scene() {
        for (int i = 0; i < 32; i++) {
            entitySetList.add(new LinkedHashSet<>());
        }
    }

    /**
     * Adds all {@linkplain Entity Entities} passed in as varargs to this {@code Scene}. Finally, notifies all
     * {@linkplain SceneObserver SceneObservers} of this {@code Scene} about the change.
     *
     * @param entities the varargs of {@code Entities} to be added to this {@code Scene}.
     * @throws NullPointerException  if any of the {@code Entities} specified in the varargs is {@code null}.
     * @throws IllegalStateException if any of the passed in {@code Entities} is already present in this {@code Scene}.
     */
    public synchronized void addEntities(Entity... entities) {
        for (Entity entity : entities) {
            addEntity(entity);
        }
    }

    /**
     * Adds an {@linkplain Entity} to this {@code Scene}. More formally adds the passed in {@code Entity} if it is not
     * already present in this {@code Scene}. Then sets the {@code Scene} of the passed in {@code Entity} to be this
     * {@code Scene} and at the end activates the {@code Entity}. Finally, notifies all
     * {@linkplain SceneObserver SceneObservers} of this {@code Scene} about the change.
     *
     * @param entity the {@code Entity} to be added to this {@code Scene}.
     * @throws NullPointerException  if the passed in {@code Entity} is {@code null}.
     * @throws IllegalStateException if the passed in {@code Entity} is already present in this {@code Scene}.
     */
    public synchronized void addEntity(Entity entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        if (!entitySet.add(entity)) {
            throw new IllegalStateException("entity is already present in this Scene");
        }
        if (entity.isActive()) {
            throw new IllegalArgumentException("cannot add an active Entity");
        }

        // Update the unmodifiableEntitySet
        unmodifiableEntitySet = Collections.unmodifiableSet(entitySet);

        // Add entity to the entitySetList depending on its layer
        entitySetList.get(entity.getLayer()).add(entity);

        // Update the unmodifiableEntitySetList
        unmodifiableEntitySetList = getUnmodifiableListSet(entitySetList);

        // Add components present in entity to the componentMap for which there exists a corresponding key
        componentMap.keySet().forEach(clazz -> {
            if (entity.hasComponent(clazz)) {
                componentMap.get(clazz).addAll(entity.getComponents(clazz));

                // Update the unmodifiableComponentMap
                unmodifiableComponentMap.put(clazz, Collections.unmodifiableSet(componentMap.get(clazz)));
            }
        });

        // Add components present in entity to the layeredComponentMap for which there exists a corresponding key
        layeredComponentMap.keySet().forEach(clazz -> {
            if (entity.hasComponent(clazz)) {
                layeredComponentMap.computeIfAbsent(clazz, c -> {
                    List<Set<Component>> list = new ArrayList<>(32);
                    for (int i = 0; i < 32; i++) {
                        list.add(new HashSet<>());
                    }
                    return list;
                }).get(entity.getLayer()).addAll(entity.getComponents(clazz));

                // Update the unmodifiableLayeredComponentMap
                unmodifiableLayeredComponentMap.put(clazz, getUnmodifiableListSet(layeredComponentMap.get(clazz)));
            }
        });

        // Add entity to entityMap if it contains sets of components for which there exist corresponding keys
        entityMap.keySet().forEach(classes -> {
            if (entity.hasComponents(classes)) {
                entityMap.get(classes).add(entity);

                // Update unmodifiableEntityMap
                unmodifiableEntityMap.put(classes, Collections.unmodifiableSet(entityMap.get(classes)));
            }
        });

        // Add entity to layeredEntityMap if it contains sets of components for which there exist corresponding keys
        layeredEntityMap.keySet().forEach(classes -> {
            if (entity.hasComponents(classes)) {
                layeredEntityMap.computeIfAbsent(classes, c -> {
                    List<Set<Entity>> list = new ArrayList<>(32);
                    for (int i = 0; i < 32; i++) {
                        list.add(new HashSet<>());
                    }
                    return list;
                }).get(entity.getLayer()).add(entity);

                // Update the unmodifiableLayeredEntityMap
                unmodifiableLayeredEntityMap.put(classes, getUnmodifiableListSet(layeredEntityMap.get(classes)));
            }
        });

        // Add EntityObserver to entity
        entity.addEntityObserver(new EntityObserver() {
            @Override
            public void componentAdded(Entity e, Component component) {
                if (componentMap.containsKey(component.getClass())) {
                    componentMap.get(component.getClass()).add(component);

                    // Update unmodifiableComponentMap
                    unmodifiableComponentMap.put(
                            component.getClass(), Collections.unmodifiableSet(componentMap.get(component.getClass())));
                }
                if (layeredComponentMap.containsKey(component.getClass())) {
                    layeredComponentMap.get(component.getClass()).get(entity.getLayer()).add(component);

                    // Update unmodifiableLayeredComponentMap
                    unmodifiableLayeredComponentMap.put(
                            component.getClass(), getUnmodifiableListSet(layeredComponentMap.get(component.getClass())));
                }
                entityMap.keySet().forEach(classes -> {
                    if (classes.contains(component.getClass()) && entity.hasComponents(classes)) {
                        entityMap.get(classes).add(entity);

                        // Update unmodifiableEntityMap
                        unmodifiableEntityMap.put(classes, Collections.unmodifiableSet(entityMap.get(classes)));
                    }
                });
                layeredEntityMap.keySet().forEach(classes -> {
                    if (classes.contains(component.getClass()) && entity.hasComponents(classes)) {
                        layeredEntityMap.get(classes).get(entity.getLayer()).add(entity);

                        // Update unmodifiableLayeredEntityMap
                        unmodifiableLayeredEntityMap.put(classes, getUnmodifiableListSet(layeredEntityMap.get(classes)));
                    }
                });
            }

            @Override
            public void componentRemoved(Entity e, Component component) {
                if (componentMap.containsKey(component.getClass())) {
                    componentMap.get(component.getClass()).remove(component);

                    // Update unmodifiableComponentMap
                    unmodifiableComponentMap.put(
                            component.getClass(), Collections.unmodifiableSet(componentMap.get(component.getClass())));
                }
                if (layeredComponentMap.containsKey(component.getClass())) {
                    layeredComponentMap.get(component.getClass()).get(entity.getLayer()).remove(component);

                    // Update unmodifiableLayeredComponentMap
                    unmodifiableLayeredComponentMap.put(
                            component.getClass(), getUnmodifiableListSet(layeredComponentMap.get(component.getClass())));
                }
                entityMap.keySet().forEach(classes -> {
                    if (classes.contains(component.getClass())) {
                        entityMap.get(classes).remove(entity);

                        // Update unmodifiableEntityMap
                        unmodifiableEntityMap.put(classes, Collections.unmodifiableSet(entityMap.get(classes)));
                    }
                });
                layeredEntityMap.keySet().forEach(classes -> {
                    if (classes.contains(component.getClass())) {
                        layeredEntityMap.get(classes).get(entity.getLayer()).remove(entity);

                        // Update unmodifiableLayeredEntityMap
                        unmodifiableLayeredEntityMap.put(classes, getUnmodifiableListSet(layeredEntityMap.get(classes)));
                    }
                });
            }

            @Override
            public void nameChanged(Entity e, String oldName, String newName) {
            }

            @Override
            public void layerChanged(Entity e, int oldLayer, int newLayer) {
                // Update entitySetList
                entitySetList.get(oldLayer).remove(entity);
                entitySetList.get(newLayer).add(entity);

                // Update unmodifiableEntitySetList
                unmodifiableEntitySetList = getUnmodifiableListSet(entitySetList);

                // Update layeredComponentMap
                layeredComponentMap.keySet().forEach(clazz -> {
                    if (entity.hasComponent(clazz)) {
                        entity.getComponents(clazz).forEach(c -> {
                            layeredComponentMap.get(clazz).get(oldLayer).remove(c);
                            layeredComponentMap.get(clazz).get(newLayer).add(c);
                        });

                        // Update unmodifiableLayeredComponentMap
                        unmodifiableLayeredComponentMap.put(clazz, getUnmodifiableListSet(layeredComponentMap.get(clazz)));
                    }
                });

                // Update layeredEntityMap
                layeredEntityMap.keySet().forEach(classes -> {
                    if (e.hasComponents(classes)) {
                        layeredEntityMap.get(classes).get(oldLayer).remove(entity);
                        layeredEntityMap.get(classes).get(newLayer).add(entity);

                        // Update unmodifiableLayeredEntityMap
                        unmodifiableLayeredEntityMap.put(classes, getUnmodifiableListSet(layeredEntityMap.get(classes)));
                    }
                });
            }

            @Override
            public void sceneChanged(Entity e, Scene oldScene, Scene newScene) {
                if (!Scene.this.equals(newScene)) {
                    clearEntityFromScene(entity);
                    entity.removeEntityObserver(this);
                }
            }

            @Override
            public void tagChanged(Entity e, String oldTag, String newTag) {
                System.out.println("tag changed");
            }

            @Override
            public void entityActivated(Entity e) {
            }

            @Override
            public void entityDeactivated(Entity e) {
            }
        });

        // Set Entity values
        entity.setScene(this);
        entity.activate();

        // Call the appropriate method on all scene observers
        sceneObserverList.forEach(sceneObserver -> sceneObserver.entityAdded(entity));
    }

    /**
     * Removes all {@linkplain Entity Entities} passed in as varargs from this {@code Scene}. Finally, notifies all
     * {@linkplain SceneObserver SceneObservers} of this {@code Scene} about the change.
     *
     * @param entities the varargs of {@code Entities} to be removed from this {@code Scene}.
     * @throws NullPointerException  if any of the {@code Entities} specified in the varargs is {@code null}.
     * @throws IllegalStateException if any of the passed in {@code Entities} is already present in this {@code Scene}.
     */
    public synchronized void removeEntities(Entity... entities) {
        for (Entity entity : entities) {
            removeEntity(entity);
        }
    }

    /**
     * Removes an {@linkplain Entity} from this {@code Scene}. More formally removes the passed in {@code Entity} if it
     * is present in this {@code Scene}. Then deactivates the passed in {@code Entity} and at the end sets the
     * {@code Scene} of the passed in {@code Entity} to {@code null}. Finally, notifies all
     * {@linkplain SceneObserver SceneObservers} of this {@code Scene} about the change.
     *
     * @param entity the {@code Entity} to be removed from this {@code Scene}.
     * @throws NullPointerException  if the passed in {@code Entity} is {@code null}.
     * @throws IllegalStateException if the passed in {@code Entity} is not present in this {@code Scene}.
     */
    public synchronized void removeEntity(Entity entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        if (!entitySet.contains(entity)) {
            throw new IllegalArgumentException("entity is not part of this scene");
        }
        if (!this.equals(entity.getScene())) {
            throw new IllegalArgumentException("the scene of the entity is not set to this scene");
        }
        if (entity.isActive()) {
            entity.deactivate();
        }
        entity.setScene(null);

        // Call the appropriate method on all scene observers
        sceneObserverList.forEach(sceneObserver -> sceneObserver.entityRemoved(entity));
    }

    /**
     * Helper method used to clear the {@code Scene} when deleting an {@linkplain Entity}.
     *
     * @param entity the {@code Entity} to delete.
     */
    private void clearEntityFromScene(Entity entity) {
        entitySet.remove(entity);

        // Update unmodifiableEntitySet
        unmodifiableEntitySet = Collections.unmodifiableSet(entitySet);

        entitySetList.get(entity.getLayer()).remove(entity);

        // Update unmodifiableEntitySetList
        unmodifiableEntitySetList = getUnmodifiableListSet(entitySetList);

        // Remove components present in entity from the entityMap for which there exists a key in the componentMap
        componentMap.keySet().forEach(clazz -> {
            if (entity.hasComponent(clazz)) {
                entity.getComponents(clazz).forEach(componentMap.get(clazz)::remove);

                // Update unmodifiableComponentMap
                unmodifiableComponentMap.put(clazz, Collections.unmodifiableSet(componentMap.get(clazz)));
            }
        });

        // Remove components present in entity from the layeredEntityMap for which there exists a corresponding key
        layeredComponentMap.keySet().forEach(clazz -> {
            if (entity.hasComponent(clazz)) {
                entity.getComponents(clazz).forEach(c -> layeredComponentMap.get(clazz).get(entity.getLayer()).remove(c));

                // Update unmodifiableLayeredComponentMap
                unmodifiableLayeredComponentMap.put(clazz, getUnmodifiableListSet(layeredComponentMap.get(clazz)));
            }
        });

        // Remove entity from entityMap if it contains sets of components for which there are keys in the entityMap
        entityMap.keySet().forEach(classes -> {
            if (entity.hasComponents(classes)) {
                entityMap.get(classes).remove(entity);

                // Update unmodifiableEntityMap
                unmodifiableEntityMap.put(classes, Collections.unmodifiableSet(entityMap.get(classes)));
            }
        });

        // Remove entity from layeredEntityMap if it contains sets of components for which there exist corresponding keys
        layeredEntityMap.keySet().forEach(classes -> {
            if (entity.hasComponents(classes)) {
                layeredEntityMap.get(classes).get(entity.getLayer()).remove(entity);

                // Update unmodifiableLayeredEntityMap
                unmodifiableLayeredEntityMap.put(classes, getUnmodifiableListSet(layeredEntityMap.get(classes)));
            }
        });
    }

    /**
     * Clears the {@code Scene} cache used to speed up data requests.
     */
    public void clearSceneCache() {
        componentMap.clear();
        entityMap.clear();
        unmodifiableComponentMap.clear();
        unmodifiableEntityMap.clear();
        layeredComponentMap.clear();
        layeredEntityMap.clear();
        unmodifiableLayeredComponentMap.clear();
        unmodifiableLayeredEntityMap.clear();
    }

    /**
     * Adds a {@linkplain SceneObserver} to this {@code Scene}.
     *
     * @param sceneObserver the {@code SceneObserver} to be added to this {@code Scene}.
     * @throws NullPointerException if the specified {@code SceneObserver} is {@code null}.
     */
    public void addSceneObserver(SceneObserver sceneObserver) {
        Objects.requireNonNull(sceneObserver, "sceneObserver must not be null");
        sceneObserverList.add(sceneObserver);
    }

    /**
     * Removes a {@linkplain SceneObserver} from this {@code Scene}.
     *
     * @param sceneObserver the {@code SceneObserver} to be removed from this {@code Scene}.
     * @throws NullPointerException if the specified {@code SceneObserver} is {@code null}.
     */
    public void removeSceneObserver(SceneObserver sceneObserver) {
        Objects.requireNonNull(sceneObserver, "sceneObserver must not be null");
        sceneObserverList.remove(sceneObserver);
    }

    /**
     * Returns an unmodifiable {@linkplain Set} of all {@linkplain Entity Entities} present in this {@code Scene}.
     *
     * @return an unmodifiable {@code Set} of all {@code Entities} present in this {@code Scene}.
     */
    public Set<Entity> getAllEntities() {
        return unmodifiableEntitySet;
    }

    /**
     * Returns an unmodifiable {@linkplain List} of unmodifiable {@linkplain Set Sets} of all
     * {@linkplain Entity Entities} present in this {@code Scene}, grouped by the layer of the {@code Entities}.
     *
     * @return an unmodifiable {@code List} of unmodifiable {@code Sets} of all {@code Entities}, grouped by the
     * layer of the {@code Entities}.
     */
    public List<Set<Entity>> getAllEntitiesByLayer() {
        return unmodifiableEntitySetList;
    }

    /**
     * Returns an unmodifiable {@linkplain Set} of all {@linkplain Component Components} of the specified type from all
     * {@linkplain Entity Entities} present in this {@code Scene}.
     *
     * @param clazz the {@linkplain Class} encapsulating the type of the wished {@code Components}.
     * @param <T>   the {@code Component} class.
     * @return an unmodifiable {@code Set} of all {@code Components} of the specified type from all {@code Entities}
     * present in this {@code Scene}.
     * @throws NullPointerException if the specified {@code Class} encapsulating the {@code Component} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    public synchronized <T extends Component> Set<T> getComponents(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Set<T> components = (Set<T>) unmodifiableComponentMap.get(clazz);
        if (components != null) {
            return components;
        }
        components = (Set<T>) componentMap.computeIfAbsent(clazz, k -> new HashSet<>());
        for (Entity entity : entitySet) {
            if (entity.hasComponent(clazz)) {
                components.addAll(entity.getComponents(clazz));
            }
        }
        Set<Component> unmodifiableComponents = Collections.unmodifiableSet(componentMap.get(clazz));
        unmodifiableComponentMap.put(clazz, unmodifiableComponents);
        return (Set<T>) unmodifiableComponents;
    }

    /**
     * Returns an unmodifiable {@linkplain List} of {@linkplain Set Sets} of all {@linkplain Component Components} of
     * the specified type from all {@linkplain Entity Entities} present in this {@code Scene}, grouped by the layers of
     * the {@code Entities}.
     *
     * @param clazz the {@linkplain Class} encapsulating the type of the wished {@code Components}.
     * @param <T>   the {@code Component} class.
     * @return an unmodifiable {@code List} of {@code Sets} of all {@code Components} of
     * the specified type from all {@code Entities}, grouped by the layers of the {@code Entities}.
     */
    @SuppressWarnings("unchecked")
    public synchronized <T extends Component> List<Set<T>> getComponentsByLayer(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        List<Set<T>> components = (List<Set<T>>) (Object) unmodifiableLayeredComponentMap.get(clazz);
        if (components != null) {
            return components;
        }
        components = (List<Set<T>>) (Object) layeredComponentMap.computeIfAbsent(clazz, k -> {
            List<Set<Component>> list = new ArrayList<>(32);
            for (int i = 0; i < 32; i++) {
                list.add(new HashSet<>());
            }
            return list;
        });
        for (Entity entity : entitySet) {
            if (entity.hasComponent(clazz)) {
                components.get(entity.getLayer()).addAll(entity.getComponents(clazz));
            }
        }
        List<Set<Component>> unmodifiableComponents = getUnmodifiableListSet(layeredComponentMap.get(clazz));
        unmodifiableLayeredComponentMap.put(clazz, unmodifiableComponents);
        return (List<Set<T>>) (Object) unmodifiableComponents;
    }

    /**
     * Returns an unmodifiable {@linkplain Set} of all {@linkplain Entity Entities} present in this {@code Scene}, which
     * contain {@linkplain Component Components} of types specified in the varargs.
     *
     * @param componentClasses the varargs of {@linkplain Class} types of the specified {@code Components}.
     * @return an unmodifiable {@code Set} of all {@code Entities} present in this {@code Scene}, which
     * contain {@code Components} of types specified in the varargs.
     * @throws NullPointerException     if any of the {@code Classes} encapsulating the {@code Component} types
     *                                  specified in the varargs is {@code null}.
     * @throws IllegalArgumentException if the varargs contains duplicates.
     */
    @SafeVarargs
    public synchronized final Set<Entity> getEntitiesWithComponents(Class<? extends Component>... componentClasses) {
        Set<Class<? extends Component>> classSet;
        try {
            classSet = Set.of(componentClasses);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("componentClasses must not contain duplicates");
        } catch (NullPointerException e) {
            throw new NullPointerException("componentClasses must not contain null");
        }
        Set<Entity> entities = unmodifiableEntityMap.get(classSet);
        if (entities != null) {
            return entities;
        }
        entities = entityMap.computeIfAbsent(classSet, k -> new HashSet<>());
        for (Entity entity : entitySet) {
            if (entity.hasComponents(componentClasses)) {
                entities.add(entity);
            }
        }
        Set<Entity> unmodifiableEntities = Collections.unmodifiableSet(entityMap.get(classSet));
        unmodifiableEntityMap.put(classSet, unmodifiableEntities);
        return unmodifiableEntities;
    }

    /**
     * Returns an unmodifiable {@linkplain List} of {@linkplain Set Sets} of all {@linkplain Entity Entities} present
     * in this {@code Scene}, which contain {@linkplain Component Components} of types specified in the varargs, grouped
     * by the layers of the {@code Entities}.
     *
     * @param componentClasses the varargs of {@linkplain Class} types of the specified {@code Components}.
     * @return an unmodifiable {@code List} of {@code Sets} of all {@code Entities} present in this {@code Scene}, which
     * contain {@code Components} of types specified in the varargs, grouped by the layers of the {@code Entities}.
     * @throws NullPointerException     if any of the {@code Classes} encapsulating the {@code Component} types
     *                                  specified in the varargs is {@code null}.
     * @throws IllegalArgumentException if the varargs contains duplicates.
     */
    @SafeVarargs
    public synchronized final List<Set<Entity>> getEntitiesWithComponentsByLayer(Class<? extends Component>... componentClasses) {
        Set<Class<? extends Component>> classSet;
        try {
            classSet = Set.of(componentClasses);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("componentClasses must not contain duplicates");
        } catch (NullPointerException e) {
            throw new NullPointerException("componentClasses must not contain null");
        }
        List<Set<Entity>> entities = unmodifiableLayeredEntityMap.get(classSet);
        if (entities != null) {
            return entities;
        }
        entities = layeredEntityMap.computeIfAbsent(classSet, k -> {
            List<Set<Entity>> list = new ArrayList<>(32);
            for (int i = 0; i < 32; i++) {
                list.add(new HashSet<>());
            }
            return list;
        });
        for (Entity entity : entitySet) {
            if (entity.hasComponents(componentClasses)) {
                entities.get(entity.getLayer()).add(entity);
            }
        }
        List<Set<Entity>> unmodifiableEntities = getUnmodifiableListSet(layeredEntityMap.get(classSet));
        unmodifiableLayeredEntityMap.put(classSet, unmodifiableEntities);
        return unmodifiableEntities;
    }

    /**
     * Helper method used to return a nested unmodifiable {@linkplain List} of unmodifiable {@linkplain Set Sets}.
     *
     * @param listSet the {@code List} of {@code Sets} to make an unmodifiable version of.
     * @param <T>     the type of the data stored in the {@code Sets}.
     * @return a nested unmodifiable {@linkplain List} of unmodifiable {@linkplain Set Sets}
     */
    private <T> List<Set<T>> getUnmodifiableListSet(List<Set<T>> listSet) {
        return listSet.stream().map(Collections::unmodifiableSet).toList();
    }
}