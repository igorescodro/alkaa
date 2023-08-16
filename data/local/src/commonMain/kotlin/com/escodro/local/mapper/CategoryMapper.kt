package com.escodro.local.mapper

import com.escodro.local.Category as LocalCategory
import com.escodro.repository.model.Category as RepoCategory

/**
 * Maps Category between Repository and Local.
 */
internal class CategoryMapper {

    /**
     * Maps Category from Repo to Local.
     *
     * @param repoCategory the Category to be converted.
     *
     * @return the converted Category
     */
    fun fromRepo(repoCategory: RepoCategory): LocalCategory =
        LocalCategory(
            category_id = repoCategory.id,
            category_name = repoCategory.name,
            category_color = repoCategory.color,
        )

    /**
     * Maps Category from Repo to Local.
     *
     * @param repoCategoryList the list of Category to be converted.
     *
     * @return the converted list of Category
     */
    fun fromRepo(repoCategoryList: List<RepoCategory>): List<LocalCategory> =
        repoCategoryList.map { fromRepo(it) }

    /**
     * Maps Category from Local to Repo.
     *
     * @param localCategory the Category to be converted.
     *
     * @return the converted Category
     */
    fun toRepo(localCategory: LocalCategory): RepoCategory =
        RepoCategory(
            id = localCategory.category_id,
            name = localCategory.category_name,
            color = localCategory.category_color,
        )

    /**
     * Maps Category from Local to Repo.
     *
     * @param localCategoryList the list of Category to be converted.
     *
     * @return the converted list of Category
     */
    fun toRepo(localCategoryList: List<LocalCategory>): List<RepoCategory> =
        localCategoryList.map { toRepo(it) }
}
