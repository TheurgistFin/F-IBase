insert into 
  OAuthClient (clientId, clientSecret, name, type, redirectUrl, serviceUser_id)
values 
  ('user-client-id', 'user-client-secret', 'test-user-client', 'USER', 'https://dev.forgeandillusion.net:8443/fake-redirect', null),
  ('admin-client-id', 'admin-client-secret', 'test-admin-client', 'USER', 'https://dev.forgeandillusion.net:8443/fake-redirect', null),
  ('guest-client-id', 'admin-guest-secret', 'test-guest-client', 'USER', 'https://dev.forgeandillusion.net:8443/fake-redirect', null);
  
insert into
  OAuthAuthorizationCode (user_id, client_id, code)
values
  (2, (select id from OAuthClient where name = 'test-user-client'), 'auth-code'),
  (4, (select id from OAuthClient where name = 'test-admin-client'), 'admin-auth-code'),
  (1, (select id from OAuthClient where name = 'test-guest-client'), 'guest-auth-code');
  
insert into
  OAuthAccessToken (accessToken, expires, authorizationCode_id, client_id)
values 
  ('access-token', 64060588800, (select id from OAuthAuthorizationCode where code = 'auth-code'), (select id from OAuthClient where name = 'test-user-client')),
  ('admin-access-token', 64060588800, (select id from OAuthAuthorizationCode where code = 'admin-auth-code'), (select id from OAuthClient where name = 'test-admin-client')),
  ('guest-access-token', 64060588800, (select id from OAuthAuthorizationCode where code = 'guest-auth-code'), (select id from OAuthClient where name = 'test-guest-client'));