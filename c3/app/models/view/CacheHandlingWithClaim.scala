package models.view

import models.view.cache.EncryptedCacheHandling

/**
 * Creates an object which has access to methods for saving to and getting
 * from the cache. The CachedClaim trait implements the cacheKey method required
 * for getting from the cache.
 * See Language (controller) and LanguageSpec (tests) for example
 * usage.
 */
class CacheHandlingWithClaim extends EncryptedCacheHandling with CachedClaim
