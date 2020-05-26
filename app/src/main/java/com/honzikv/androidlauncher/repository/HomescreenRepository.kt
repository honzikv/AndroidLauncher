package com.honzikv.androidlauncher.repository

import androidx.lifecycle.LiveData
import com.honzikv.androidlauncher.database.dao.FolderDao
import com.honzikv.androidlauncher.database.dao.PageDao
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.FolderModel
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.model.PageWithFolders
import com.honzikv.androidlauncher.utils.Exception.IntegrityException
import timber.log.Timber
import java.lang.Exception

/**
 * Repository pro upravu modelovych dat homescreenu - slozky a stranky
 */
class HomescreenRepository(
    private val pageDao: PageDao,
    private val folderDao: FolderDao
) {

    val allPagesWithFolders = pageDao.getAllPagesWithFoldersLiveData()

    val allPages = pageDao.getAllPagesLiveData()

    /**
     * Prida novou prazdnou stranku na konec
     * @return id pridane stranky
     */
    suspend fun addPageAsLast(): Long {
        val lastPage = pageDao.getLastPageNumber()
        return if (lastPage == null) {
            pageDao.addPage(PageModel(pageNumber = 0))
        } else {
            pageDao.addPage(PageModel(pageNumber = lastPage + 1))
        }
    }

    /**
     * Prida novou prazdnou stranku na zacatek
     * @return id pridane stranky
     */
    suspend fun addPageAsFirst(): Long {
        val pages = pageDao.getAllPages()
        Timber.d("pages size = ${pages.size}")
        pages.forEach { it.pageNumber = it.pageNumber + 1 }
        pageDao.updatePageList(pages)
        //PageModel has default value of position as 0
        return pageDao.addPage(PageModel())
    }

    /**
     * Nastavi slozce s id [folderId] stranku s id [pageId]
     */
    suspend fun addFolderToPage(folderId: Long, pageId: Long) {
        val page = pageDao.getPage(pageId)
        val folder = folderDao.getFolder(folderId)

        folder.position = page.nextFolderPosition
        folder.pageId = page.id
        page.nextFolderPosition = page.nextFolderPosition + 1

        pageDao.updatePage(page)
        folderDao.updateFolder(folder)
    }

    /**
     * Odstrani stranku s id [pageId] z databaze
     */
    suspend fun removePage(pageId: Long) {
        val pages = pageDao.getAllPages()

        //Zablokuje odstraneni posledni stranky
        if (pages.size == 1) {
            throw IntegrityException("Cannot delete last page")
        }

        //K zajisteni integrity je potreba zmenit pozice stranek
        var pageIndex = -1
        for (i in pages.indices) {
            if (pages[i].id == pageId) {
                pageIndex = i
                break
            }
        }

        if (pageIndex == -1) {
            return
        }

        val page = pages[pageIndex]
        for (i in pageIndex + 1 until pages.size) {
            pages[i].pageNumber = pages[i].pageNumber - 1
        }
        pages.removeAt(pageIndex)
        pageDao.updatePageList(pages)
        pageDao.deletePage(page)
    }

    /**
     * Aktualizuje slozku v databazi
     */
    suspend fun updateFolder(folderModel: FolderModel) = folderDao.updateFolder(folderModel)

    /**
     * Smaze slozku s id [folderId] z databaze
     */
    suspend fun deleteFolder(folderId: Long) {
        val folder = folderDao.getFolder(folderId)
        if (folder.pageId != null) {
            //pro stranku je potreba zmenit pozice ostatnich slozek aby se zachovala integrita db
            val pageWithFolders = pageDao.getPageWithFolders(folder.pageId!!)
            val folders = pageWithFolders.folderList

            var folderIndex = -1
            for (i in folders.indices) {
                if (folders[i].folder.id == folderId) {
                    folderIndex = i
                    break
                }
            }
            for (i in folderIndex + 1 until folders.size) {
                folders[i].folder.position = folders[i].folder.position!! - 1
                folderDao.updateFolder(folders[i].folder)
            }
        }
        folderDao.deleteFolderWithId(folderId)
    }

    /**
     * Smaze predmet slozky s id [folderItemId] z databaze
     */
    suspend fun deleteFolderItem(folderItemId: Long) {
        val folderItem = folderDao.getFolderItem(folderItemId)
        if (folderItem.folderId != null) {
            //Pro ostatni aplikace je potreba ve slozce zmenit pozice aby se zachovala integrita db
            val folder = folderDao.getFolderWithItems(folderItem.folderId!!)
            val items = folder.itemList

            var index = -1
            for (i in items.indices) {
                if (items[i].id == folderItemId) {
                    index = i
                    break
                }
            }
            for (i in index + 1 until items.size) {
                items[i].position = items[i].position - 1
                folderDao.updateFolderItem(items[i])
            }
        }
        folderDao.deleteFolderItem(folderItemId)
    }

    /**
     * Ziska LiveData obsahujici [PageWithFolders] objekt pro [pageId]
     */
    fun getPageWithFolders(pageId: Long): LiveData<PageWithFolders> =
        pageDao.getPageWithFoldersLiveData(pageId)

    /**
     * Smaze slozku s id [folderId]
     */
    suspend fun deleteFolderWithId(folderId: Long) = folderDao.deleteFolderWithId(folderId)

    /**
     * Aktualizuje vsechny slozky predane v parametru [folders]
     */
    suspend fun updateFolders(vararg folders: FolderModel) = folderDao.updateFolders(*folders)

    /**
     * Aktualizuje seznam vsech slozek predany v parametru [folderList]
     */
    suspend fun updateFolderList(folderList: List<FolderModel>) = folderDao.updateFolderList(folderList)

    /**
     * Vrati [FolderWithItems] s danym id [folderId] slozky
     */
    suspend fun getFolderWithItems(folderId: Long) = folderDao.getFolderWithItems(folderId)

    /**
     * Prida predmety [items], ktere jiz maji ulozenou referenci na slozku
     */
    suspend fun addItemsWithFolderId(items: List<FolderItemModel>) =
        folderDao.insertItemsWithFolderId(items)

    /**
     * Ziska [FolderWithItems] jako LiveData objekt pro dane id slozky [folderId]
     */
    fun getFolderWithItemsLiveData(folderId: Long) = folderDao.getFolderWithItemsLiveData(folderId)

    /**
     * Aktualizuje vsechny predane argumenty [items]
     */
    suspend fun updateFolderItems(vararg items: FolderItemModel) =
        folderDao.updateFolderItems(*items)

    /**
     * Ziska prvni stranku a vrati ji jako [PageModel]
     */
    suspend fun getFirstPage() = pageDao.getFirstPage()

    /**
     * Prida slozku [folder] bez reference na stranku
     */
    suspend fun addFolderWithoutPage(folder: FolderModel) = folderDao.addFolderWithoutPage(folder)

    /**
     * Aktualizuje [itemList] - seznam slozek
     */
    suspend fun updateFolderItemList(itemList: List<FolderItemModel>) =
        folderDao.updateFolderItemList(itemList)

    /**
     * Aktualizuje [itemList] - seznam stranek
     */
    suspend fun updatePageList(itemList: List<PageModel>) = pageDao.updatePageList(itemList)

}