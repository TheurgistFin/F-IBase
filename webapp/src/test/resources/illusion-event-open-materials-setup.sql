insert into   
  Material (id, created, modified, publicity, title, type, urlName, creator_id, modifier_id, parentFolder_id)
values 
  (20050, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('1 1 2010', 'd M yyyy'), 'PRIVATE', 'Document', 'DOCUMENT', 'document', 2, 2, 20000),
  (20051, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('1 1 2010', 'd M yyyy'), 'PRIVATE', 'Image', 'IMAGE', 'image', 2, 2, 20000),
  (20052, PARSEDATETIME('1 1 2010', 'd M yyyy'), PARSEDATETIME('1 1 2010', 'd M yyyy'), 'PRIVATE', 'Vector Image', 'VECTOR_IMAGE', 'vectorimage', 2, 2, 20000);

insert into 
  Document (id, data)
values 
  (20050, '<p>Document in root</p>');

insert into 
  Binary_ (id, contentType, data)
values 
  (20051, 'image/jpeg', X'ffd8ffe000104a46494600010101012c012c0000ffe10be64578696600004d4d002a000000080007011200030000000100010000011a00050000000100000062011b0005000000010000006a012800030000000100020000013100020000000b0000007201320002000000140000007e876900040000000100000092000000d40000012c000000010000012c0000000147494d5020322e382e360000323031343a30343a32332030373a30333a3535000005900000070000000430323130a00000070000000430313030a00100030000000100010000a002000400000001000000c8a003000400000001000000b7000000000006010300030000000100060000011a00050000000100000122011b0005000000010000012a012800030000000100020000020100040000000100000132020200040000000100000aac0000000000000048000000010000004800000001ffd8ffe000104a46494600010100000100010000ffdb00430050373c463c32504641465a55505f78c882786e6e78f5afb991c8ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffdb004301555a5a786978eb8282ebffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc000110800a700b703012200021101031101ffc4001f0000010501010101010100000000000000000102030405060708090a0bffc400b5100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9faffc4001f0100030101010101010101010000000000000102030405060708090a0bffc400b51100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a82838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae2e3e4e5e6e7e8e9eaf2f3f4f5f6f7f8f9faffda000c03010002110311003f00407d69d51ed93d57f2a4c483b03408969bbb6c80ff00b269a2423a8228ff005878ed40135b8c2b7d71fa54a7a5321fb99f524d2c87087d4f14c0adfc44527dd901ec7834a3ae7d691c6578eb4fa00ac3069c9f771e9c527de50d9ebda88cf26a407d2c4332907b8a4a58b8947d0d0059a28a4a00456dc4fa0a7520c76a5a0028a293b9a0041d4d3a9077a5a0029a3ef139ec05293804d449e9ead401301814514500671723d0fd09a50643f779fc41a969081d40e6818d0d2778cd3d0e43310463a51b8e376e34ec1f954f52726800c1585867a714c77df18fa7eb5237dddbead519032cc3eea7f3a62065c05f6e2929fc32fd698460e0d500d8c0f997d0d397effe14d1f2ca0faf14ee8d52c07d03efafd69acdb464d08af21c93b57b5202de4671de900ea4f535181b4753f89a4190bc360fb5004c0606296a25978c9e47a8a96800a407233413fad2d0020ef4b49de96801adce07ad32307cc39e833fafff005a9fc6e2c4f1d29c2800a2918e071d68a00a8485c64d2d203f3ae4679c539210a32dcfb76a06323219b1c601c9a70605b79e067029cab1b0c0503f0a6a42a304e703d6801c4f05fb9fbb4f0988b69fc6990fce777f0af0296673f747738a62234383b695c719a197b8a4cee53ed54031c7cb9ee39a7039c11de90f22914fee87e552c07850ed93f757f53526e34d0005dbd4514803a734e4eb4c670bf5a679ded40164003a52c670767e22abacd938e952124e08fbcb4013e39cd2d22e768cf5c52d0035b8707d8d29e052367e5faf34ea00638c44453c74a6c9c21a527e5a003a907b0a291d822ff21450055419907d6a7c76ed50c5feb0f3f854bc7419eb40c8c29600742bc669eca704939c0e94b8e723f1a53c8c50057569238fcc38e7b7ad2b1c953ed9fce9d2e1e3d8bc9ed4d906180f45029a11253186d3b80fad2a1c8c7714eaa021149160803dc9a3eeb914c46d849a96058cf38c54723e3814c3231f6a6d201caa58d4c2d323249a640406e6af8e94019d245e59a512e17919ab171866c0a54b75dbcd00491b6f8d5bd453aa386331295ce46722a4a0043d29690f434018a004619207bd0bca2d3bbd373b63fc2802263ba43e83814534650e1fa9ef4532d0c87fd71fc6a7c0c9f5aac870cc7b75a9c48093814891c7a544ee4640e4d399fd3eb9a646430f72fcfbd004b147b17d4f7a89c67e6ab150f51548445d391535427a53d55f197240ec051702295bf7981e94ca9650aa3818c9a4813736f2320741eb520464103241028abad82b8350b44872470680210706a513b018cd33ca6f6fce8f29fd07e7400e5932f935751c30ace2aebd54d4b6f27ef40cd005e3494514008dca9a514119a0734009fc79f6a1802a41a504127da9ad220c82c3e9401046de62ed71cd15193b30d8e07f2a29944e403dbbd1b1718c52d07a5210df2d3fba291d3a15e08e869f41200c9a0011f23e6e0f715087c96080b73c50ecb2636f23b9a9005e36f007a517111aa6d6dce416ec3d295892c3d69cc72699d013de802195b2c7daaca011c6a3daaaa8dcea3d4d5a6eb40099e6a292421b038a930339f4a8e55248205002c4c581cd3ce7b1c53635dabcf5a479369c0eb40126ec734144906e1f2b7a8eb4c8df7039ed4f06801f1484111c9f7bb1f5a9aa0203af3d453a2909043fde5fd68025a2807201a28022de4332ae339cf351afde3b860f5a571be46c71b7f5346eca86ee3ad343108ec68a5c8070dd3b1a2980f1d39a3b52d15203471927a7ad3154484b3723b0a73fcc76f61c9a13a50214608f97814d2c4e4118a731c0a6500149d6968a04418db30fad4f4c7c1c7a834fa062020f4a296928011ce149a8304826a693ee9a89180c83d0d003e1230477a96ab1183c1a9d4ee506810f53834487690e3a8fd45252b1c8140cb0082011d0d23305527d05456cdc14cfdd34e9bee607f11c500471719cf53c9a43f2bfb1e0d2affac61ec2958656a8620195da7a8a2909db86edd0d140121381934d66d8b93c9cd3aa39ff00817d4d4807afb9c9a70202839a809da1cf61d2a68c010ae7d3340818e4fb53480460d2d2119a005a8a3387606a5a8243b65045004ac320d3a9072296800a28a2810544d1024ed383e95231da3268041e868195981538618a7c726de3a8a9880c304546611db8a007ab061914ea6226c1ef4fa0420e0e4707d6a5dfbf9f4ed51d00ed607b74342180ff005df5069fea2987fd6afd4d3f387fa8aa188bdc1a2871de8a0071e78a87efb97ec3815237caa5bb814d036a2afa0a906453f098f5a981c44bf4a8273dbdaa443945fa5021d4d63b4669d4c93ee9a007d4538f941f434f8db72d2b2ee523d6801b11ca63d29f557953b4f514f5948ebcd004d4b51f9a3d0d2198761408908c8c1a81a364395e47f2a0c8c7bd264e08cf5a063926ec6a55756e86ab9191480faf5a00b745409211c1e454c0e466810b4633c1ef4503ad031a80b3293d89cd48fd6900c4cdee3343f5aa18e1f30a29aa7068a00593fd591eb48dd689380a3de90d48995a5396269d049c6c3f8546fd0d201f2fbd005bdc3d45432be781d0533f84376a6939e05004f07434f660b8cd3611f29a26195cfa5002c881d78ebd8d5707b1eb52c4fced3f8524c9cee1d2801941a419e8696800a28a5009190280128ee334628a005230714f89bf84fe151e72307a8fe545005aa298ae0a64f6a1c9c00a393dfd280099ff0078bb79352375a8c460291d49ea69431030dd40ebeb4d00ea2abbb97e070b453b8c9d9b7487d178a6c8d84fad145488ae46452d1450022b00c437dd3530887af145140120000c0a493ee37d28a2802b8192077ab08d9c83d475a28a0063459e56a320a9c1a28a042a26efa54e0003028a28002011cd432205231d28a28008e3def8ce3e5cd35815620f5145140c9625c2f3de9e060628a28016a099b736dec28a2801945145007fffd9ffe106a2687474703a2f2f6e732e61646f62652e636f6d2f7861702f312e302f003c3f787061636b657420626567696e3d27efbbbf272069643d2757354d304d7043656869487a7265537a4e54637a6b633964273f3e0a3c783a786d706d65746120786d6c6e733a783d2761646f62653a6e733a6d6574612f273e0a3c7264663a52444620786d6c6e733a7264663d27687474703a2f2f7777772e77332e6f72672f313939392f30322f32322d7264662d73796e7461782d6e7323273e0a0a203c7264663a4465736372697074696f6e20786d6c6e733a786d703d27687474703a2f2f6e732e61646f62652e636f6d2f7861702f312e302f273e0a20203c786d703a43726561746f72546f6f6c3e41646f62652050686f746f73686f7020435335204d6163696e746f73683c2f786d703a43726561746f72546f6f6c3e0a20203c786d703a437265617465446174653e323031332d30392d30325431363a33393a31352b30333a30303c2f786d703a437265617465446174653e0a20203c786d703a4d65746164617461446174653e323031332d30392d31365431343a33383a30392b30333a30303c2f786d703a4d65746164617461446174653e0a20203c786d703a4d6f64696679446174653e323031332d30392d31365431343a33383a30392b30333a30303c2f786d703a4d6f64696679446174653e0a203c2f7264663a4465736372697074696f6e3e0a0a203c7264663a4465736372697074696f6e20786d6c6e733a64633d27687474703a2f2f7075726c2e6f72672f64632f656c656d656e74732f312e312f273e0a20203c64633a666f726d61743e696d6167652f6a7065673c2f64633a666f726d61743e0a203c2f7264663a4465736372697074696f6e3e0a0a203c7264663a4465736372697074696f6e20786d6c6e733a786d704d4d3d27687474703a2f2f6e732e61646f62652e636f6d2f7861702f312e302f6d6d2f273e0a20203c786d704d4d3a496e7374616e636549443e786d702e6969643a30363830313137343037323036383131383843363943413339333941313837423c2f786d704d4d3a496e7374616e636549443e0a20203c786d704d4d3a4f726967696e616c446f63756d656e7449443e786d702e6469643a30313830313137343037323036383131393130394643433541433745463231423c2f786d704d4d3a4f726967696e616c446f63756d656e7449443e0a20203c786d704d4d3a496e7374616e636549443e786d702e6969643a30363830313137343037323036383131383843363943413339333941313837423c2f786d704d4d3a496e7374616e636549443e0a20203c786d704d4d3a446f63756d656e744944207264663a7265736f757263653d27786d702e6469643a303138303131373430373230363831313931303946434335414337454632314227202f3e0a20203c786d704d4d3a4f726967696e616c446f63756d656e7449443e786d702e6469643a30313830313137343037323036383131393130394643433541433745463231423c2f786d704d4d3a4f726967696e616c446f63756d656e7449443e0a20203c786d704d4d3a486973746f72793e0a2020203c7264663a5365713e0a2020203c2f7264663a5365713e0a20203c2f786d704d4d3a486973746f72793e0a20203c786d704d4d3a4465726976656446726f6d207264663a7061727365547970653d275265736f75726365273e0a20203c2f786d704d4d3a4465726976656446726f6d3e0a203c2f7264663a4465736372697074696f6e3e0a0a203c7264663a4465736372697074696f6e20786d6c6e733a70686f746f73686f703d27687474703a2f2f6e732e61646f62652e636f6d2f70686f746f73686f702f312e302f273e0a20203c70686f746f73686f703a436f6c6f724d6f64653e333c2f70686f746f73686f703a436f6c6f724d6f64653e0a20203c70686f746f73686f703a49434350726f66696c653e735247422049454336313936362d322e313c2f70686f746f73686f703a49434350726f66696c653e0a20203c70686f746f73686f703a446f63756d656e74416e636573746f72733e0a2020203c7264663a5365713e0a202020203c7264663a6c693e786d702e6469643a30313830313137343037323036383131393130394643433541433745463231423c2f7264663a6c693e0a2020203c2f7264663a5365713e0a20203c2f70686f746f73686f703a446f63756d656e74416e636573746f72733e0a203c2f7264663a4465736372697074696f6e3e0a0a3c2f7264663a5244463e0a3c2f783a786d706d6574613e0a3c3f787061636b657420656e643d2772273f3e0affe20c584943435f50524f46494c4500010100000c484c696e6f021000006d6e74725247422058595a2007ce00020009000600310000616373704d5346540000000049454320735247420000000000000000000000010000f6d6000100000000d32d4850202000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001163707274000001500000003364657363000001840000006c77747074000001f000000014626b707400000204000000147258595a00000218000000146758595a0000022c000000146258595a0000024000000014646d6e640000025400000070646d6464000002c400000088767565640000034c0000008676696577000003d4000000246c756d69000003f8000000146d6561730000040c0000002474656368000004300000000c725452430000043c0000080c675452430000043c0000080c625452430000043c0000080c7465787400000000436f70797269676874202863292031393938204865776c6574742d5061636b61726420436f6d70616e790000646573630000000000000012735247422049454336313936362d322e31000000000000000000000012735247422049454336313936362d322e31000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000058595a20000000000000f35100010000000116cc58595a200000000000000000000000000000000058595a200000000000006fa2000038f50000039058595a2000000000000062990000b785000018da58595a2000000000000024a000000f840000b6cf64657363000000000000001649454320687474703a2f2f7777772e6965632e636800000000000000000000001649454320687474703a2f2f7777772e6965632e63680000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000064657363000000000000002e4945432036313936362d322e312044656661756c742052474220636f6c6f7572207370616365202d207352474200000000000000000000002e4945432036313936362d322e312044656661756c742052474220636f6c6f7572207370616365202d20735247420000000000000000000000000000000000000000000064657363000000000000002c5265666572656e63652056696577696e6720436f6e646974696f6e20696e2049454336313936362d322e3100000000000000000000002c5265666572656e63652056696577696e6720436f6e646974696f6e20696e2049454336313936362d322e31000000000000000000000000000000000000000000000000000076696577000000000013a4fe00145f2e0010cf140003edcc0004130b00035c9e0000000158595a2000000000004c09560050000000571fe76d6561730000000000000001000000000000000000000000000000000000028f0000000273696720000000004352542063757276000000000000040000000005000a000f00140019001e00230028002d00320037003b00400045004a004f00540059005e00630068006d00720077007c00810086008b00900095009a009f00a400a900ae00b200b700bc00c100c600cb00d000d500db00e000e500eb00f000f600fb01010107010d01130119011f0125012b01320138013e0145014c0152015901600167016e0175017c0183018b0192019a01a101a901b101b901c101c901d101d901e101e901f201fa0203020c0214021d0226022f02380241024b0254025d02670271027a0284028e029802a202ac02b602c102cb02d502e002eb02f50300030b03160321032d03380343034f035a03660372037e038a039603a203ae03ba03c703d303e003ec03f9040604130420042d043b0448045504630471047e048c049a04a804b604c404d304e104f004fe050d051c052b053a05490558056705770586059605a605b505c505d505e505f6060606160627063706480659066a067b068c069d06af06c006d106e306f507070719072b073d074f076107740786079907ac07bf07d207e507f8080b081f08320846085a086e0882089608aa08be08d208e708fb09100925093a094f09640979098f09a409ba09cf09e509fb0a110a270a3d0a540a6a0a810a980aae0ac50adc0af30b0b0b220b390b510b690b800b980bb00bc80be10bf90c120c2a0c430c5c0c750c8e0ca70cc00cd90cf30d0d0d260d400d5a0d740d8e0da90dc30dde0df80e130e2e0e490e640e7f0e9b0eb60ed20eee0f090f250f410f5e0f7a0f960fb30fcf0fec1009102610431061107e109b10b910d710f511131131114f116d118c11aa11c911e81207122612451264128412a312c312e31303132313431363138313a413c513e5140614271449146a148b14ad14ce14f01512153415561578159b15bd15e0160316261649166c168f16b216d616fa171d17411765178917ae17d217f7181b18401865188a18af18d518fa19201945196b199119b719dd1a041a2a1a511a771a9e1ac51aec1b141b3b1b631b8a1bb21bda1c021c2a1c521c7b1ca31ccc1cf51d1e1d471d701d991dc31dec1e161e401e6a1e941ebe1ee91f131f3e1f691f941fbf1fea20152041206c209820c420f0211c2148217521a121ce21fb22272255228222af22dd230a23382366239423c223f0241f244d247c24ab24da250925382568259725c725f726272657268726b726e827182749277a27ab27dc280d283f287128a228d429062938296b299d29d02a022a352a682a9b2acf2b022b362b692b9d2bd12c052c392c6e2ca22cd72d0c2d412d762dab2de12e162e4c2e822eb72eee2f242f5a2f912fc72ffe3035306c30a430db3112314a318231ba31f2322a3263329b32d4330d3346337f33b833f1342b3465349e34d83513354d358735c235fd3637367236ae36e937243760379c37d738143850388c38c839053942397f39bc39f93a363a743ab23aef3b2d3b6b3baa3be83c273c653ca43ce33d223d613da13de03e203e603ea03ee03f213f613fa23fe24023406440a640e74129416a41ac41ee4230427242b542f7433a437d43c044034447448a44ce45124555459a45de4622466746ab46f04735477b47c04805484b489148d7491d496349a949f04a374a7d4ac44b0c4b534b9a4be24c2a4c724cba4d024d4a4d934ddc4e254e6e4eb74f004f494f934fdd5027507150bb51065150519b51e65231527c52c75313535f53aa53f65442548f54db5528557555c2560f565c56a956f75744579257e0582f587d58cb591a596959b85a075a565aa65af55b455b955be55c355c865cd65d275d785dc95e1a5e6c5ebd5f0f5f615fb36005605760aa60fc614f61a261f56249629c62f06343639763eb6440649464e9653d659265e7663d669266e8673d679367e9683f689668ec6943699a69f16a486a9f6af76b4f6ba76bff6c576caf6d086d606db96e126e6b6ec46f1e6f786fd1702b708670e0713a719571f0724b72a67301735d73b87414747074cc7528758575e1763e769b76f8775677b37811786e78cc792a798979e77a467aa57b047b637bc27c217c817ce17d417da17e017e627ec27f237f847fe5804780a8810a816b81cd8230829282f4835783ba841d848084e3854785ab860e867286d7873b879f8804886988ce8933899989fe8a648aca8b308b968bfc8c638cca8d318d988dff8e668ece8f368f9e9006906e90d6913f91a89211927a92e3934d93b69420948a94f4955f95c99634969f970a977597e0984c98b89924999099fc9a689ad59b429baf9c1c9c899cf79d649dd29e409eae9f1d9f8b9ffaa069a0d8a147a1b6a226a296a306a376a3e6a456a4c7a538a5a9a61aa68ba6fda76ea7e0a852a8c4a937a9a9aa1caa8fab02ab75abe9ac5cacd0ad44adb8ae2daea1af16af8bb000b075b0eab160b1d6b24bb2c2b338b3aeb425b49cb513b58ab601b679b6f0b768b7e0b859b8d1b94ab9c2ba3bbab5bb2ebba7bc21bc9bbd15bd8fbe0abe84beffbf7abff5c070c0ecc167c1e3c25fc2dbc358c3d4c451c4cec54bc5c8c646c6c3c741c7bfc83dc8bcc93ac9b9ca38cab7cb36cbb6cc35ccb5cd35cdb5ce36ceb6cf37cfb8d039d0bad13cd1bed23fd2c1d344d3c6d449d4cbd54ed5d1d655d6d8d75cd7e0d864d8e8d96cd9f1da76dafbdb80dc05dc8add10dd96de1cdea2df29dfafe036e0bde144e1cce253e2dbe363e3ebe473e4fce584e60de696e71fe7a9e832e8bce946e9d0ea5beae5eb70ebfbec86ed11ed9cee28eeb4ef40efccf058f0e5f172f1fff28cf319f3a7f434f4c2f550f5def66df6fbf78af819f8a8f938f9c7fa57fae7fb77fc07fc98fd29fdbafe4bfedcff6dffffffdb00430050373c463c32504641465a55505f78c882786e6e78f5afb991c8ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffdb004301555a5a786978eb8282ebffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc200110800b700c803011100021101031101ffc40017000003010000000000000000000000000000010203ffc4001501010100000000000000000000000000000001ffda000c0301000210031000000148c04334208a202800d404300010c043300018cd0cc9a4540303500000000020b3054302d190aac0700c0d443000000112598a83829a252c40286324d400a0000010c0c9410c6894b19228605000c650008621818a80228a455204950c602280a1808621818ad92324d122810822c4492059a1402188625c8d09288251d512208a200d9526451b0000088592c604824d50198a0019baa1252b40000442c9a01208ea00a3280620181a1aaa400000c964b01250549254414592480846e5000008c96c000480864906c2200b10c46a0004094aa800044a512224d0448142194234003257415008408c40220d0000c86680302c445a015008494210012508008028a011a92b3401501209448800450001988658c450ed9188a89252800404960000211006a00359aa11510080c000c8d404311250c4300105b401128806324a33194301124145140002b680225108b1106823218c04002194580005a0e126649a0883510198c0000000b28082ed4381332462036200448c0000000d04312c96819880a28a2093411032c649051268303311ffc4001e10000104030101010000000000000000000100101120303141022140ffda000801010001050273aeaf4c29dc9f5a58316ea0ddcb2a5bb3f7ae328da8a060e2b2a72f18edbadac5ca415d41173801a9bf50bcb841fa8b15d78fa2a28022186edd1bc8104500a2dcd21b6282368a838034b7468a2d08af2f0a1416f3aac8a430f88add89429aca1ce86d8a0d2fac7151ba8a063a193b60e58a1f848b061b73a42a2f0a2a175cb79af6b383b42bce9ba8a9aeda54d45385797ed254d853ac748b4a97947107eb1636282233f69b50c5e2f1f285bfffc40014110100000000000000000000000000000090ffda0008010301013f010fff00ffc40014110100000000000000000000000000000090ffda0008010201013f010fff00ffc4001c1000020105010000000000000000000000114001102131506070ffda0008010100063f02e37152c862efc292ccedcf283d9fffc4002510000202020203000203010100000000000001101121312041516171308191a1b1d1e1ffda0008010100013f219d86e3a47650b50bfccb79e2b4a7b8f833e20f284cafdc3e685a348c6690b5fc6b55ee2feff11a8a9b551d899bfed0f46d5cbb51df17a97e0ffa450aad32fd05bbad1d18f6c7b6c71d90b7179a59137d96b12778e7db12ae1ab1e29e4bc22b286369d0ec79c0a859656458f90a143c419e0bcd5192f05bd90b1909dbdb32c098cd0f62d7b7c2cda2c5ee7b8d27bfdc5bcfd2d6fa1bc1bd0f5068e87a16a4fc25acd9f16874375e450efe9465736f914347536ec424c70ed67df1ec5a46d9c1ea6e8aa4ddb8598f22c4ec3607c44d1f27815d32b8a5edee4b71dc365d4d97f867ebf816c562c4ce8937ad987b7140c6938dfca3f7175bc0d6bc51ee1ffec514f052a3a1e874a8ce98f62c6a36fe9a8c835cda786537b5af0276ad704de90b93aec4edecd28c066b2bd8a3742ca1337b5aecbd7b9edb45e2cf4e7b1bcd22a8d4b57ca7966d28c5ff00486a666869fd36abc4bc09661f0aae25b1aa1ae11e5e23548ec33bb9ec438ec7a1651e38f80be84e9f07ac31ecd91b5518397d0d9667b97a1fae6d18d90aef83742ff2763dc755e4eecd5fde3ae5b274c4ef97b16a3d9d42cb6ff4686cfb3da374c5c163a1f04c2e2fee311d0b088d4eca5b83568d619658b72695b34c38d833713a76507e11d1b5096bf02d7c86b96ef0fe471a46c216e14dbae225653e08b1db75d158a2eb0c6efe0ddfc0de258abe8549ade04ec7e03542562550d4696a2b152d6ea3ffda000c03010002000300000010b6dabf64da6db6db6da4d257f4db6d369269b4410bdd9349a6da6db680a707b26d26da69a4d100b2b6e9b6d34da4d304939fe920534db49a449226dba28269b6d2441043ff0064c329369a6d9008db649064b2d34da000ebec9241345a69ae087576d36934db4d274805ecdb4d86da6db5010404d34d941a49b7480494906800404536c12484900116d000b68920129149a0034882902412922d240025b402a4821240140a2102002c121968a2c804b4c005804a08b01b2000d200ae0169b2c96824934406c90926d06da0da09a49ca936d82d22082124904fffc4001b11010002020300000000000000000000000120400060105070ffda0008010301013f10915de0aeef0593b220da70f2622e9ad66055ffc4001b11010100030003000000000000000000000140101150003060ffda0008010201013f10f434b398672932cc6598cbce292930b46f927d1949d3df4c7c6affc400261001000202010401040301000000000000010011213110415161718120a1b1c191d1f0e1ffda0008010100013f101efcfd845051ea7e17f7c689b3dcfe0ea25a9dc8ac3c1b077473400dbf4fdb1c866f80fcc0ad4f4ff32cedfc303a71ee210ef3155a57eb8b3e03f73447c03860acbf69f938c57dfe39a2efaf2e08641868f5c2d0af496b2db47f7c2cecf718767ed2dd92914b5e84144efa2ffbe261ac0b3f70b0b7cbdb2aacf30db2186d3678ba4f30d4dd3a1b808b7d7e8d9ce6794574ed7f75716ecbf982d83c39251eb7fbd4adf0026ddcb61be807fdf8812fa9961cdf4e35ec3367ae1bd157889a4856a9858aa6ba388aee5d6794c9e381b2f82d4d51440b5758e0b9d38bb5b466583af6fdc5156f44a211a32faffb2c23d4c278894d4d2f4710a82b0997ed0e20f6f78bbe950572d9de13231dc3ab5e3808676ef856a3d18b45c6f971c741dd8da81ab609c8b3706b3e1de36d5bae9a961a10c1181ee0f88c68e9d7f12ac3373d4eaf6cc50cb5572e5cef37d3c72627ac6989443edf1c817ce77fa897345f73f3c1a1773340dc005d159fe650abbaa232c14e457a9a92ae9dfba8fe0fe2656bb59764dcd97b3325ea65fe35c5d16b15c6117baac147492979212e0a5e6b6ee5719d0eb16c798c00dfd8417b75fd4c2fa899e681728743bca2bc8182604601511e6bbf155a7cc5b7ea3bf822d2ab89ac6a1b600c99819129bc43436b0395aaf706e2587b0cfb622f84c111ef7fa8b968662094eb84aa0b7b45a95d920f52205912c4974424626f7c2a81768385c272caeb34df2d62fbe381ca7696c7b0a8d569d8ff00737f8b9e218288aaaaed704cae6b513a225353ef9168b62da7a2dcea1d5815aaa0df033ac5ae65605d4112ce5ca1c1b78429e958dea50f720a00fbc71ad66b30938bc3f0f59f6d73609a1ee52abfe501be4dac4587625258b5d78222b3125acd55cb744f99fe07fa88683d1965415e65839719ab785659de218adf6960b0c0d3c4727fc94ec4f0a2d4a2bb44f60d3084b4dc4c429ddfd401acbbbde0d2f623647745051c214311d53c161bccb155fb26c17ae0c92c7e810018b5bf6c68a25236fcc4a6b92ab1d78444a40cfad7d21d455f31c189a041e8662db28b1ea4d11417de00b65a04df1d062a9b9a7ee52a99e8e517000f7da46c0f4dcd3d0e8c35aae3a0b8c86abcac156e5eeb14a296f4c45b78a9152f5f4a6e9a79566621568cfec71e0821a439a730ca3a80b56f0e55165196576ca2ed33174fa2829d6f800d1512c8f6ef181bd465e3a32a53b3874c54d76ae0882699f269f6cc59f786c19743bb0f037716cbd03ef2d6a6e2a38cdbb468937150f722239dd3f4a0d8b3b4b57e12a183659c2589de355acd0ca25eda9a3e9958481628cf8a2a144a742a327d0c472caf8cf582d1313d9f5264cc273bef3c911015be76532cb5b3ef31f6fda1b1f330b47f98eceeb3ea27a026490e57e8ddeef9df09654c41074dfd5749d987d33ec1fd4c0be2398db5d37323ef4f51538f9038bc706be043a0712ef5ca0db1627f9894e567980f8e8c070e1fa006c749505e5d31378fa45a4f6cc1e8a2c0ef17f25f0e49458e9c130ca575208d353cd14ebf42753851a5c73a20837ad315b0c45abb54dabb4575ed1a84181b875dd2f30d3d46976a7be3507a3be1fa40b3afe1c50a767034dcad551ac35369adc781dd38db3be0ba9d62d40bede0523de5f96cd4a95ab1fb42cf2728a8896c78ba6c8e87a31537298ecc3d13abcca68c428134618eff00096274c3e65178e605130aaddc237aea81e4f1a0798146892a5fc2417384454c65500638345acc3afa635ec56902da268e5e2471ffd9');

insert into Image (id) values (20051);

insert into 
  VectorImage (id, data) 
values
  (20052, '<?xml version="1.0" encoding="UTF-8" standalone="no"?><svg width="100" height="100"><circle r="45" cy="50" cx="50" style="fill:yellow;stroke: #000"/></svg>');