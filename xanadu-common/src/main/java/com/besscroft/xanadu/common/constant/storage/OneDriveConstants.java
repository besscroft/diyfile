package com.besscroft.xanadu.common.constant.storage;

/**
 * @Description OneDrive 常量
 * @Author Bess Croft
 * @Date 2023/1/20 16:15
 */
public interface OneDriveConstants {

    /** 驱动器 ID URL */
    String DRIVE_ID_URL = "https://graph.microsoft.com/v1.0/me/drive/root?select=parentReference";

    /** 我的驱动器中的所有项 */
    String DRIVE_ROOT_URL = "/children?select=name,size,lastModifiedDateTime,file,@microsoft.graph.downloadUrl,@odata.nextLink,value";

    /** 列出带已知路径的 DriveItem 子项 */
    String DRIVE_ITEM_URL = "https://graph.microsoft.com/v1.0/me/drives/{drive-id}/root:{path-relative-to-root}:/children?select=name,size,lastModifiedDateTime,file,@microsoft.graph.downloadUrl,@odata.nextLink,value";

    /** 使用刷新令牌获取新的访问令牌 */
    String AUTHENTICATE_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/token";

    /** 获取单个项目文件 */
    String DRIVE_FILE_URL = "https://graph.microsoft.com/v1.0/me/drive/root:{path}?select=name,size,lastModifiedDateTime,file,@microsoft.graph.downloadUrl";

    /** 上载会话创建 API */
    String DRIVE_UPLOAD_SESSION_URL = "https://graph.microsoft.com/v1.0/me/drive/root:{path}:/createUploadSession";

}
