create table Address (
    id bigint generated by default as identity,
    addressType integer not null,
    city varchar(255) not null,
    postalCode varchar(255) not null,
    street1 varchar(255) not null,
    street2 varchar(255),
    country_id bigint,
    user_id bigint,
    primary key (id)
);

create table Binary_ (
    contentType varchar(255),
    data blob,
    id bigint not null,
    primary key (id)
);

create table BlogCategory (
    id bigint generated by default as identity,
    name varchar(255) not null,
    nextSync timestamp,
    sync varchar(255) not null,
    syncUrl varchar(255),
    primary key (id)
);

create table BlogEntry (
    id bigint generated by default as identity,
    authorName varchar(255),
    content clob,
    created timestamp not null,
    guid varchar(255) not null,
    link varchar(255),
    modified timestamp not null,
    summary clob not null,
    title varchar(255) not null,
    urlName varchar(255),
    category_id bigint,
    creator_id bigint,
    modifier_id bigint,
    primary key (id)
);

create table BlogEntryTag (
    id bigint generated by default as identity,
    entry_id bigint,
    tag_id bigint,
    primary key (id)
);

create table BlogTag (
    id bigint generated by default as identity,
    text varchar(255) not null,
    primary key (id)
);

create table BookPublication (
    downloadCount bigint not null,
    numberOfPages integer,
    printCount bigint not null,
    id bigint not null,
    downloadableFile_id bigint,
    printableFile_id bigint,
    primary key (id)
);

create table CoOpsSession (
    id bigint generated by default as identity,
    accessed timestamp not null,
    algorithm varchar(255) not null,
    closed boolean not null,
    joinRevision bigint not null,
    sessionId varchar(255) not null,
    type varchar(255) not null,
    material_id bigint,
    user_id bigint,
    primary key (id)
);

create table Country (
    id bigint generated by default as identity,
    code varchar(255) not null,
    name varchar(255) not null,
    primary key (id)
);

create table Document (
    data clob,
    id bigint not null,
    primary key (id)
);

create table DocumentRevision (
    id bigint not null,
    document_id bigint,
    primary key (id)
);

create table DropboxFile (
    dropboxPath varchar(255) not null,
    mimeType varchar(255) not null,
    id bigint not null,
    primary key (id)
);

create table DropboxFolder (
    dropboxPath varchar(255) not null,
    id bigint not null,
    primary key (id)
);

create table DropboxRootFolder (
    deltaCursor varchar(255),
    lastSynchronized timestamp,
    id bigint not null,
    primary key (id)
);

create table File (
    id bigint not null,
    primary key (id)
);

create table Folder (
    id bigint not null,
    primary key (id)
);

create table Forum (
    id bigint generated by default as identity,
    allowTopicCreation BIT not null,
    description clob,
    name varchar(255) not null,
    urlName varchar(255) not null,
    category_id bigint,
    primary key (id)
);

create table ForumCategory (
    id bigint generated by default as identity,
    name varchar(255) not null,
    primary key (id)
);

create table ForumMessage (
    id bigint generated by default as identity,
    created timestamp not null,
    modified timestamp not null,
    views bigint not null,
    author_id bigint,
    primary key (id)
);

create table ForumPost (
    content clob not null,
    id bigint not null,
    topic_id bigint,
    primary key (id)
);

create table ForumTopic (
    subject varchar(255) not null,
    urlName varchar(255) not null,
    id bigint not null,
    forum_id bigint,
    primary key (id)
);

create table ForumTopicWatcher (
    id bigint generated by default as identity,
    topic_id bigint,
    user_id bigint,
    primary key (id)
);

create table FriendConfirmKey (
    id bigint generated by default as identity,
    created timestamp not null,
    value varchar(255) not null,
    friend_id bigint,
    user_id bigint,
    primary key (id)
);

create table GameLibraryTag (
    id bigint generated by default as identity,
    text varchar(255) not null,
    primary key (id)
);

create table GoogleDocument (
    documentId varchar(255) not null,
    mimeType varchar(255) not null,
    id bigint not null,
    primary key (id)
);

create table IllusionFolder (
    id bigint not null,
    primary key (id)
);

create table IllusionGroup (
    id bigint generated by default as identity,
    created timestamp not null,
    description clob,
    name varchar(255) not null,
    urlName varchar(255) not null,
    xmppRoom varchar(255) not null,
    folder_id bigint,
    primary key (id)
);

create table IllusionGroupDocument (
    documentType varchar(255) not null,
    id bigint not null,
    primary key (id)
);

create table IllusionGroupFolder (
    id bigint not null,
    primary key (id)
);

create table IllusionGroupMember (
    id bigint generated by default as identity,
    characterName varchar(255) not null,
    role varchar(255),
    group_id bigint,
    user_id bigint,
    primary key (id)
);

create table IllusionGroupMemberImage (
    id bigint generated by default as identity,
    contentType varchar(255) not null,
    data blob,
    modified timestamp not null,
    member_id bigint,
    primary key (id)
);

create table IllusionGroupMemberSetting (
    id bigint generated by default as identity,
    settingKey varchar(255) not null,
    value varchar(255),
    member_id bigint,
    primary key (id)
);

create table IllusionGroupSetting (
    id bigint generated by default as identity,
    settingKey varchar(255) not null,
    value varchar(255),
    group_id bigint,
    primary key (id)
);

create table Image (
    id bigint not null,
    primary key (id)
);

create table ImageRevision (
    id bigint not null,
    image_id bigint,
    primary key (id)
);

create table InternalAuth (
    id bigint generated by default as identity,
    password varchar(255) not null,
    verified BIT not null,
    user_id bigint,
    primary key (id)
);

create table Language (
    id bigint generated by default as identity,
    ISO2 varchar(255),
    ISO3 varchar(255),
    localized BIT not null,
    primary key (id)
);

create table Material (
    id bigint generated by default as identity,
    created timestamp not null,
    modified timestamp not null,
    publicity varchar(255) not null,
    title varchar(255) not null,
    type varchar(255) not null,
    urlName varchar(255) not null,
    creator_id bigint,
    language_id bigint,
    modifier_id bigint,
    parentFolder_id bigint,
    primary key (id)
);

create table MaterialRevision (
    id bigint generated by default as identity,
    checksum varchar(255),
    completeRevision BIT not null,
    compressed BIT not null,
    created timestamp not null,
    data blob,
    revision bigint not null,
    sessionId varchar(255),
    primary key (id)
);

create table MaterialRevisionSetting (
    id bigint generated by default as identity,
    value varchar(255),
    key_id bigint,
    materialRevision_id bigint,
    primary key (id)
);

create table MaterialRevisionTag (
    id bigint generated by default as identity,
    removed BIT not null,
    materialRevision_id bigint,
    tag_id bigint,
    primary key (id)
);

create table MaterialSetting (
    id bigint generated by default as identity,
    value varchar(255) not null,
    key_id bigint,
    material_id bigint,
    primary key (id)
);

create table MaterialSettingKey (
    id bigint generated by default as identity,
    name varchar(255) not null,
    primary key (id)
);

create table MaterialTag (
    id bigint generated by default as identity,
    material_id bigint,
    tag_id bigint,
    primary key (id)
);

create table MaterialThumbnail (
    id bigint generated by default as identity,
    content blob,
    contentType varchar(255),
    size varchar(255) not null,
    material_id bigint,
    primary key (id)
);

create table MaterialView (
    id bigint generated by default as identity,
    count integer not null,
    viewed timestamp not null,
    material_id bigint,
    user_id bigint,
    primary key (id)
);

create table Message (
    id bigint generated by default as identity,
    content clob not null,
    sent timestamp not null,
    subject varchar(255) not null,
    threadId varchar(255) not null,
    sender_id bigint,
    primary key (id)
);

create table MessageFolder (
    id bigint generated by default as identity,
    name varchar(255) not null,
    owner_id bigint,
    primary key (id)
);

create table OrderItem (
    id bigint generated by default as identity,
    count integer not null,
    name varchar(255) not null,
    unitPrice double,
    order_id bigint,
    publication_id bigint,
    primary key (id)
);

create table Order_ (
    id bigint generated by default as identity,
    accessKey varchar(255),
    canceled timestamp,
    created timestamp,
    customerCompany varchar(255),
    customerEmail varchar(255) not null,
    customerFirstName varchar(255) not null,
    customerLastName varchar(255) not null,
    customerMobile varchar(255),
    customerPhone varchar(255),
    delivered timestamp,
    notes clob,
    orderStatus varchar(255),
    paid timestamp,
    shipped timestamp,
    shippingCosts double,
    customer_id bigint,
    deliveryAddress_id bigint,
    primary key (id)
);

create table PasswordResetKey (
    id bigint generated by default as identity,
    created timestamp not null,
    value varchar(255) not null,
    user_id bigint,
    primary key (id)
);

create table Pdf (
    id bigint not null,
    primary key (id)
);

create table PermaLink (
    id bigint generated by default as identity,
    path varchar(255) not null,
    material_id bigint,
    primary key (id)
);

create table Publication (
    id bigint generated by default as identity,
    created timestamp not null,
    depth integer,
    description clob,
    height integer,
    license varchar(255) not null,
    modified timestamp not null,
    name varchar(255) not null,
    price double not null,
    published BIT not null,
    urlName varchar(255) not null,
    weight double,
    width integer,
    creator_id bigint,
    defaultImage_id bigint,
    forumTopic_id bigint,
    language_id bigint,
    modifier_id bigint,
    primary key (id)
);

create table PublicationAuthor (
    id bigint generated by default as identity,
    author_id bigint,
    publication_id bigint,
    primary key (id)
);

create table PublicationFile (
    id bigint generated by default as identity,
    content blob not null,
    contentType varchar(255) not null,
    primary key (id)
);

create table PublicationImage (
    id bigint generated by default as identity,
    content blob not null,
    contentType varchar(255) not null,
    created timestamp not null,
    modified timestamp not null,
    creator_id bigint,
    modifier_id bigint,
    publication_id bigint,
    primary key (id)
);

create table PublicationTag (
    id bigint generated by default as identity,
    publication_id bigint,
    tag_id bigint,
    primary key (id)
);

create table RecipientMessage (
    id bigint generated by default as identity,
    read_ BIT not null,
    removed BIT not null,
    starred BIT not null,
    folder_id bigint,
    message_id bigint,
    recipient_id bigint,
    primary key (id)
);

create table ShoppingCart (
    id bigint generated by default as identity,
    created timestamp not null,
    deliveryMethodId varchar(255),
    modified timestamp not null,
    sessionId varchar(255),
    customer_id bigint,
    deliveryAddress_id bigint,
    primary key (id)
);

create table ShoppingCartItem (
    id bigint generated by default as identity,
    count integer not null,
    cart_id bigint,
    publication_id bigint,
    primary key (id)
);

create table StarredMaterial (
    id bigint generated by default as identity,
    created timestamp not null,
    material_id bigint,
    user_id bigint,
    primary key (id)
);

create table SystemSetting (
    id bigint generated by default as identity,
    settingKey varchar(255) not null,
    value varchar(255) not null,
    primary key (id)
);

create table Tag (
    id bigint generated by default as identity,
    text varchar(255) not null,
    primary key (id)
);

create table User (
    id bigint generated by default as identity,
    about clob,
    archived BIT not null,
    company varchar(255),
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    locale varchar(255) not null,
    mobile varchar(255),
    nickname varchar(255),
    phone varchar(255),
    premiumExpires timestamp,
    profileImageSource varchar(255) not null,
    registrationDate timestamp not null,
    role varchar(255) not null,
    primary key (id)
);

create table UserChatCredentials (
    id bigint generated by default as identity,
    password varchar(255) not null,
    userJid varchar(255) not null,
    user_id bigint,
    primary key (id)
);

create table UserContactField (
    id bigint generated by default as identity,
    type varchar(255) not null,
    value varchar(255) not null,
    user_id bigint,
    primary key (id)
);

create table UserEmail (
    id bigint generated by default as identity,
    email varchar(255) not null,
    primaryEmail BIT not null,
    user_id bigint,
    primary key (id)
);

create table UserFriend (
    id bigint generated by default as identity,
    confirmed BIT not null,
    friend_id bigint,
    user_id bigint,
    primary key (id)
);

create table UserIdentifier (
    id bigint generated by default as identity,
    authSource varchar(255) not null,
    identifier varchar(255) not null,
    sourceId varchar(255) not null,
    user_id bigint,
    primary key (id)
);

create table UserImage (
    id bigint generated by default as identity,
    contentType varchar(255),
    data blob,
    modified timestamp not null,
    user_id bigint,
    primary key (id)
);

create table UserMaterialRole (
    id bigint generated by default as identity,
    role varchar(255),
    material_id bigint,
    user_id bigint,
    primary key (id)
);

create table UserSetting (
    id bigint generated by default as identity,
    value varchar(255) not null,
    user_id bigint,
    userSettingKey_id bigint,
    primary key (id)
);

create table UserSettingKey (
    id bigint generated by default as identity,
    key_ varchar(255) not null,
    primary key (id)
);

create table UserToken (
    id bigint generated by default as identity,
    expires timestamp,
    grantedScopes varchar(255),
    secret varchar(255),
    token varchar(1024) not null,
    userIdentifier_id bigint,
    primary key (id)
);

create table UserVerificationKey (
    id bigint generated by default as identity,
    created timestamp not null,
    value varchar(255) not null,
    user_id bigint,
    primary key (id)
);

create table VectorImage (
    data clob,
    id bigint not null,
    primary key (id)
);

create table VectorImageRevision (
    id bigint not null,
    vectorImage_id bigint,
    primary key (id)
);

alter table CoOpsSession 
    add constraint UK_gnmr24o8mycwyds1wbmlp3q3p  unique (sessionId);

alter table GameLibraryTag 
    add constraint UK_4so6mh922ymtcy90ebjebip12  unique (text);

alter table IllusionGroup 
    add constraint UK_ix2sqwg9t6xb6kcv3ovupjsh5  unique (urlName);

alter table IllusionGroup 
    add constraint UK_qad0tdjy689f23i3bsl0f4kew  unique (xmppRoom);

alter table UserEmail 
    add constraint UK_64595ikvvhrgya5f17x4y5bh9  unique (email);

alter table Address 
    add constraint FK_b9wby4b3jwmvb3t6u4m0jlygg 
    foreign key (country_id) 
    references Country;

alter table Address 
    add constraint FK_33hg5keygw64femiy5lgd4y9t 
    foreign key (user_id) 
    references User;

alter table Binary_ 
    add constraint FK_t8vmhvwngg2vtjls1gyf3vjyv 
    foreign key (id) 
    references Material;

alter table BlogEntry 
    add constraint FK_hhpchf7bv5ratxwq3jqsie5fk 
    foreign key (category_id) 
    references BlogCategory;

alter table BlogEntry 
    add constraint FK_pv1oa95stblj2x19q851906vs 
    foreign key (creator_id) 
    references User;

alter table BlogEntry 
    add constraint FK_4jxfnu2hx10gjhyr9teqpgh2s 
    foreign key (modifier_id) 
    references User;

alter table BlogEntryTag 
    add constraint FK_ncofal77nq7rmrlprnrfb2cmb 
    foreign key (entry_id) 
    references BlogEntry;

alter table BlogEntryTag 
    add constraint FK_qno46w1xh2wt3h0nlagr35e62 
    foreign key (tag_id) 
    references BlogTag;

alter table BookPublication 
    add constraint FK_ju2xorsfflj7raab3cv079l5x 
    foreign key (downloadableFile_id) 
    references PublicationFile;

alter table BookPublication 
    add constraint FK_3bg7owacmg2nva6bl6btfwmwi 
    foreign key (printableFile_id) 
    references PublicationFile;

alter table BookPublication 
    add constraint FK_truqclteayq1fpva8iubn43t5 
    foreign key (id) 
    references Publication;

alter table CoOpsSession 
    add constraint FK_wob1x94bufe9nqq8bclvnhy7 
    foreign key (material_id) 
    references Material;

alter table CoOpsSession 
    add constraint FK_gxfr8cvpe9c53dck2nt6ujrqs 
    foreign key (user_id) 
    references User;

alter table Document 
    add constraint FK_1jry86o40p7fcuo1fi564tgcj 
    foreign key (id) 
    references Material;

alter table DocumentRevision 
    add constraint FK_5c5yqhl4icfm3ts9x0dqwbreg 
    foreign key (document_id) 
    references Document;

alter table DocumentRevision 
    add constraint FK_bb2yvl5c9q98ffnxs2tyfly8l 
    foreign key (id) 
    references MaterialRevision;

alter table DropboxFile 
    add constraint FK_763v1y6sj5clsl1yck5mq2l1m 
    foreign key (id) 
    references Material;

alter table DropboxFolder 
    add constraint FK_l35xp1hpw367749yi79n66fnf 
    foreign key (id) 
    references Folder;

alter table DropboxRootFolder 
    add constraint FK_9g0c9ihbvbu0cnjcdllripp4n 
    foreign key (id) 
    references Folder;

alter table File 
    add constraint FK_17vh64nwb61tixrxnviulrva2 
    foreign key (id) 
    references Binary_;

alter table Folder 
    add constraint FK_idx3wiwdm8yp2qkkddi726n8o 
    foreign key (id) 
    references Material;

alter table Forum 
    add constraint FK_5m8qqcnsb5gsif8taa58khwwd 
    foreign key (category_id) 
    references ForumCategory;

alter table ForumMessage 
    add constraint FK_12mkuto37lpbyavjp36il7fk6 
    foreign key (author_id) 
    references User;

alter table ForumPost 
    add constraint FK_oin05xpq2f3lvp22jpu4mtppu 
    foreign key (topic_id) 
    references ForumTopic;

alter table ForumPost 
    add constraint FK_k4o4gqltnknpvkn33fhbajf07 
    foreign key (id) 
    references ForumMessage;

alter table ForumTopic 
    add constraint FK_ft3gjajs954n8do2y1vgd36m1 
    foreign key (forum_id) 
    references Forum;

alter table ForumTopic 
    add constraint FK_pqua2p3mkee2bacxf19c73v3o 
    foreign key (id) 
    references ForumMessage;

alter table ForumTopicWatcher 
    add constraint FK_3y506ftpsm1ynn859skf0x8c3 
    foreign key (topic_id) 
    references ForumTopic;

alter table ForumTopicWatcher 
    add constraint FK_n0h7phcrsoyduqwl1cndho5uy 
    foreign key (user_id) 
    references User;

alter table FriendConfirmKey 
    add constraint FK_wfblwxkfts5g3fg7ee0nfd2u 
    foreign key (friend_id) 
    references User;

alter table FriendConfirmKey 
    add constraint FK_9561x0eub9681y890bbh8oa2m 
    foreign key (user_id) 
    references User;

alter table GoogleDocument 
    add constraint FK_rohgbl8frsua16jnumxo9y7gq 
    foreign key (id) 
    references Material;

alter table IllusionFolder 
    add constraint FK_qieidhiuhk09j5m6l6pqkt0a5 
    foreign key (id) 
    references Folder;

alter table IllusionGroup 
    add constraint FK_1ccfwpe2ygxulx35ykxspqsmq 
    foreign key (folder_id) 
    references IllusionGroupFolder;

alter table IllusionGroupDocument 
    add constraint FK_i1ctgxnegc2wvdcdfwm9oruo6 
    foreign key (id) 
    references Document;

alter table IllusionGroupFolder 
    add constraint FK_qye4g9cukldbot5k5u0w6k8en 
    foreign key (id) 
    references Folder;

alter table IllusionGroupMember 
    add constraint FK_4ney1jxmm0a7qsavn23n11pbc 
    foreign key (group_id) 
    references IllusionGroup;

alter table IllusionGroupMember 
    add constraint FK_9ok316p3y0mvfqf167cudt5xu 
    foreign key (user_id) 
    references User;

alter table IllusionGroupMemberImage 
    add constraint FK_lk3e3jpg8ky1bl1kxb1ce3oat 
    foreign key (member_id) 
    references IllusionGroupMember;

alter table IllusionGroupMemberSetting 
    add constraint FK_af78vixxjq03nocl2xt85ym3w 
    foreign key (member_id) 
    references IllusionGroupMember;

alter table IllusionGroupSetting 
    add constraint FK_hdqeshicq23nolw9j160rpl0a 
    foreign key (group_id) 
    references IllusionGroup;

alter table Image 
    add constraint FK_9xcia6idnwqdi9xx8ytea40h3 
    foreign key (id) 
    references Binary_;

alter table ImageRevision 
    add constraint FK_ae927momh8i44pw15h7c8v883 
    foreign key (image_id) 
    references Image;

alter table ImageRevision 
    add constraint FK_opfsl8bg6s8k9h879ec1x5eug 
    foreign key (id) 
    references MaterialRevision;

alter table InternalAuth 
    add constraint FK_4ev60hulclv68yy2b7i7no069 
    foreign key (user_id) 
    references User;

alter table Material 
    add constraint FK_fx7vtonuv6g0a9kdbev72aj71 
    foreign key (creator_id) 
    references User;

alter table Material 
    add constraint FK_b3woqnul7laianx9030b1roh0 
    foreign key (language_id) 
    references Language;

alter table Material 
    add constraint FK_gps5qt45nwfqe1psjkov77e0g 
    foreign key (modifier_id) 
    references User;

alter table Material 
    add constraint FK_r9eehdvglf4xvj55m3o8ow2s6 
    foreign key (parentFolder_id) 
    references Folder;

alter table MaterialRevisionSetting 
    add constraint FK_ct6frqhrvcsxdx8c4kitv68wd 
    foreign key (key_id) 
    references MaterialSettingKey;

alter table MaterialRevisionSetting 
    add constraint FK_gurfakq9wbav6msh4f20tg3y7 
    foreign key (materialRevision_id) 
    references MaterialRevision;

alter table MaterialRevisionTag 
    add constraint FK_rjpc4n4ab456l6fkffs711b7f 
    foreign key (materialRevision_id) 
    references MaterialRevision;

alter table MaterialRevisionTag 
    add constraint FK_8khhkh39wfk7wy83s2b76kht 
    foreign key (tag_id) 
    references Tag;

alter table MaterialSetting 
    add constraint FK_kx5dhocydap0gugdij1yoayig 
    foreign key (key_id) 
    references MaterialSettingKey;

alter table MaterialSetting 
    add constraint FK_skdihk14gde1eefchngsftv7w 
    foreign key (material_id) 
    references Material;

alter table MaterialTag 
    add constraint FK_3js3omusv545pec4rofbsjlfj 
    foreign key (material_id) 
    references Material;

alter table MaterialTag 
    add constraint FK_1y9txx76jb95g1atbe17pvr33 
    foreign key (tag_id) 
    references Tag;

alter table MaterialThumbnail 
    add constraint FK_9s2fwcpsnmo374012ia4l7xr9 
    foreign key (material_id) 
    references Material;

alter table MaterialView 
    add constraint FK_c5t1qubcegltq6le7yg7rrgon 
    foreign key (material_id) 
    references Material;

alter table MaterialView 
    add constraint FK_kc05eno56sd0jdwu0pdbepoos 
    foreign key (user_id) 
    references User;

alter table Message 
    add constraint FK_tbto6hemu447oixxk730el2vx 
    foreign key (sender_id) 
    references User;

alter table MessageFolder 
    add constraint FK_o3f1777quj13cge2kd7llwoar 
    foreign key (owner_id) 
    references User;

alter table OrderItem 
    add constraint FK_6cxptya5vldowhtfdxs70ytw1 
    foreign key (order_id) 
    references Order_;

alter table OrderItem 
    add constraint FK_bexee46kvwhk7kmi5mirwvf94 
    foreign key (publication_id) 
    references Publication;

alter table Order_ 
    add constraint FK_nuektq0t0wsfj2yos4jaga5yq 
    foreign key (customer_id) 
    references User;

alter table Order_ 
    add constraint FK_4jexecwfjhyb7fmjbf1cpwfbx 
    foreign key (deliveryAddress_id) 
    references Address;

alter table PasswordResetKey 
    add constraint FK_ig2l7ybgill2j63l9rsdtsvhd 
    foreign key (user_id) 
    references User;

alter table Pdf 
    add constraint FK_2l0kstgj1ayjmh2968qgvmw6q 
    foreign key (id) 
    references Binary_;

alter table PermaLink 
    add constraint FK_5mh9gi6vjsvnhyny4f8489hdh 
    foreign key (material_id) 
    references Material;

alter table Publication 
    add constraint FK_4spcv9w9c2hsbhdcktsi5ahb2 
    foreign key (creator_id) 
    references User;

alter table Publication 
    add constraint FK_45r8ijdx482x9l31s0ptvdurs 
    foreign key (defaultImage_id) 
    references PublicationImage;

alter table Publication 
    add constraint FK_e2nks3e781go58t5fq804g20b 
    foreign key (forumTopic_id) 
    references ForumTopic;

alter table Publication 
    add constraint FK_9iwnkkslclyi784x9nfto9915 
    foreign key (language_id) 
    references Language;

alter table Publication 
    add constraint FK_lovvwguqn722f3upi5kehro4r 
    foreign key (modifier_id) 
    references User;

alter table PublicationAuthor 
    add constraint FK_mnr5qymj4y7kpam7gwn20gbno 
    foreign key (author_id) 
    references User;

alter table PublicationAuthor 
    add constraint FK_82528is9e0hklum9r8ol28ibw 
    foreign key (publication_id) 
    references Publication;

alter table PublicationImage 
    add constraint FK_kp5cv5yvta2aurqe9nv6gxyir 
    foreign key (creator_id) 
    references User;

alter table PublicationImage 
    add constraint FK_8apjtolm7i5qnnw9ddx916jps 
    foreign key (modifier_id) 
    references User;

alter table PublicationImage 
    add constraint FK_6jn47ypu7k2yskfs86olxwy1b 
    foreign key (publication_id) 
    references Publication;

alter table PublicationTag 
    add constraint FK_boonyg7478u8t1ypeipj1pmkl 
    foreign key (publication_id) 
    references Publication;

alter table PublicationTag 
    add constraint FK_toxebsnjvyi3o0ckg8fcfm8d0 
    foreign key (tag_id) 
    references GameLibraryTag;

alter table RecipientMessage 
    add constraint FK_jpwylts3n7aaj8glqxxfjk9f8 
    foreign key (folder_id) 
    references MessageFolder;

alter table RecipientMessage 
    add constraint FK_a5jq61n718lkefe477rbk1981 
    foreign key (message_id) 
    references Message;

alter table RecipientMessage 
    add constraint FK_29stadpyo7wj3605qp9xrwimy 
    foreign key (recipient_id) 
    references User;

alter table ShoppingCart 
    add constraint FK_226ui9my3sceiilf5pmd35g7l 
    foreign key (customer_id) 
    references User;

alter table ShoppingCart 
    add constraint FK_4oe973j8s9242kguphe3so2rx 
    foreign key (deliveryAddress_id) 
    references Address;

alter table ShoppingCartItem 
    add constraint FK_am5qynab11y7xgjbgst5ulfff 
    foreign key (cart_id) 
    references ShoppingCart;

alter table ShoppingCartItem 
    add constraint FK_1hr90hiipfmghjy3ur6x2qwc9 
    foreign key (publication_id) 
    references Publication;

alter table StarredMaterial 
    add constraint FK_etvdnq9ekq5l1g5xfyxthg3tl 
    foreign key (material_id) 
    references Material;

alter table StarredMaterial 
    add constraint FK_wbtdleyqvpwjlnc8rjmmf6s8 
    foreign key (user_id) 
    references User;

alter table UserChatCredentials 
    add constraint FK_h3ipddn5k0nejikrmpayobxcx 
    foreign key (user_id) 
    references User;

alter table UserContactField 
    add constraint FK_ftmlh7t6ki89bgge2ebmdf2li 
    foreign key (user_id) 
    references User;

alter table UserEmail 
    add constraint FK_eyclispg2p45hk6ryxn0rurq4 
    foreign key (user_id) 
    references User;

alter table UserFriend 
    add constraint FK_1moblwb5r358wnd77acxga4mn 
    foreign key (friend_id) 
    references User;

alter table UserFriend 
    add constraint FK_a01cuibcreifm5la30a329hn7 
    foreign key (user_id) 
    references User;

alter table UserIdentifier 
    add constraint FK_lnyj92dpxse5ma8bq2n8cs4cg 
    foreign key (user_id) 
    references User;

alter table UserImage 
    add constraint FK_ek8itxfnsp5le03hxbta0749w 
    foreign key (user_id) 
    references User;

alter table UserMaterialRole 
    add constraint FK_nq4i53ppmmjbrhd30vgfinwpp 
    foreign key (material_id) 
    references Material;

alter table UserMaterialRole 
    add constraint FK_lsvoxxuju09swlpvn1qcoxlik 
    foreign key (user_id) 
    references User;

alter table UserSetting 
    add constraint FK_qt91lfx3ktlndkjknu1qhykyp 
    foreign key (user_id) 
    references User;

alter table UserSetting 
    add constraint FK_opl68ghfxbq51vsl8d2c6p1v5 
    foreign key (userSettingKey_id) 
    references UserSettingKey;

alter table UserToken 
    add constraint FK_id71ppyyw0tkkj8c97n935lk4 
    foreign key (userIdentifier_id) 
    references UserIdentifier;

alter table UserVerificationKey 
    add constraint FK_icq5jrfca3mudiw4kpe9s0q4d 
    foreign key (user_id) 
    references User;

alter table VectorImage 
    add constraint FK_o0mft3m1vbns8qsy7mv72n8e6 
    foreign key (id) 
    references Material;

alter table VectorImageRevision 
    add constraint FK_8aghc2j34pfkekkyuxsk6gf7y 
    foreign key (vectorImage_id) 
    references VectorImage;

alter table VectorImageRevision 
    add constraint FK_qxq9qp6lutv7aml83qypblfaq 
    foreign key (id) 
    references MaterialRevision;