package com.jeff_media.jefflib.data;

import com.jeff_media.jefflib.JeffLib;
import com.jeff_media.jefflib.internal.annotations.Internal;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a {@link PersistentDataContainer} from an {@link org.bukkit.OfflinePlayer}. <b>Important:</b> When changing values, you have to call {@link #save()} or {@link #saveAsync()} afterwards.
 */
public class OfflinePlayerPersistentDataContainer implements PersistentDataContainer {

    private final PersistentDataContainer craftPersistentDataContainer;
    private final File file;
    private final Object compoundTag;

    public OfflinePlayerPersistentDataContainer(@Nonnull PersistentDataContainer craftPersistentDataContainer, @Nonnull File file, @Nonnull Object compoundTag) {
        this.craftPersistentDataContainer = craftPersistentDataContainer;
        this.file = file;
        this.compoundTag = compoundTag;
    }

    /**
     * For internal use only
     * @deprecated For internal use only
     */
    @Deprecated
    @Internal
    public Object getCraftPersistentDataContainer() {
        return craftPersistentDataContainer;
    }

    /**
     * For internal use only
     * @deprecated For internal use only
     */
    @Deprecated
    @Internal
    public Object getCompoundTag() {
        return compoundTag;
    }

    public File getFile() {
        return file;
    }

    /**
     * Saves the data to the player's file. This will overwrite any changes made to the original file that happened after
     * creating this instance.
     */
    public void save() {
        try {
            JeffLib.getNMSHandler().updatePdcInDatFile(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the data to the player's file. This will overwrite any changes made to the original file that happened after
     * creating this instance.
     */
    public CompletableFuture<Void> saveAsync() {
        return CompletableFuture.runAsync(this::save);
    }

    @Override
    public <T, Z> void set(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType, @Nonnull Z z) {
        craftPersistentDataContainer.set(namespacedKey, persistentDataType, z);
    }

    @Override
    public <T, Z> boolean has(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType) {
        return craftPersistentDataContainer.has(namespacedKey, persistentDataType);
    }

    public boolean has(@Nonnull NamespacedKey namespacedKey) {
        return craftPersistentDataContainer.getKeys().contains(namespacedKey);
    }

    @Override
    public <T, Z> Z get(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType) {
        return craftPersistentDataContainer.get(namespacedKey, persistentDataType);
    }

    @Override
    @Nonnull
    public <T, Z> Z getOrDefault(@Nonnull NamespacedKey namespacedKey, @Nonnull PersistentDataType<T, Z> persistentDataType, @Nonnull Z z) {
        return craftPersistentDataContainer.getOrDefault(namespacedKey, persistentDataType, z);
    }

    @Override
    @Nonnull
    public Set<NamespacedKey> getKeys() {
        return craftPersistentDataContainer.getKeys();
    }

    /**
     * <b>Not supported</b>
     * @throws UnsupportedOperationException This PersistentDataContainer is read-only
     */
    @Override
    public void remove(@Nonnull NamespacedKey namespacedKey) {
        craftPersistentDataContainer.remove(namespacedKey);
    }

    @Override
    public boolean isEmpty() {
        return craftPersistentDataContainer.isEmpty();
    }

    @Override
    @Nonnull
    public PersistentDataAdapterContext getAdapterContext() {
        return craftPersistentDataContainer.getAdapterContext();
    }
}
