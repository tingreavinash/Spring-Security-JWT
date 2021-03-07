package com.avinash.springsecurityjwt.controller;

import com.avinash.springsecurityjwt.model.AuthenticationRequest;
import com.avinash.springsecurityjwt.model.AuthenticationResponse;
import com.avinash.springsecurityjwt.service.MyUserDetailsService;
import com.avinash.springsecurityjwt.service.PropertyConfiguration;
import com.avinash.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
public class MyController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Environment environment;

    @Value("${app.username}")
    private String propertyValue;

    @Autowired
    private PropertyConfiguration myConfig;

    /***
     *
     * User below header:
     * Key = Authorization
     * Value = "Bearer <JWT_Token>"
     * @return Hello String!
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String Hello() {
        return "Hello Spring !";
    }

    /***
     * Read application.properties using Environment class autowiring.
     * @param key
     * @return
     */
    @RequestMapping(value= "/readKeyUsingEnvironment", method = RequestMethod.GET)
    public ResponseEntity<String> readKeyUsingEnvironment(@RequestParam(value = "name") String key){
        String value="";
        value = environment.getProperty(key);
        return ResponseEntity.ok(value);
    }

    /***
     * Read application.properties using Value annotation.
     *
     * @return
     */
    @RequestMapping(value= "/readKeyUsingValue", method = RequestMethod.GET)
    public ResponseEntity<String> readKeyUsingValue(){

        return ResponseEntity.ok(propertyValue);
    }

    /***
     * Read application.properties using ConfigurationProperties class.
     * @return
     */
    @RequestMapping(value= "/readKeyUsingConfiguration", method = RequestMethod.GET)
    public ResponseEntity<String> readKeyUsingConfiguration(){
        String username = myConfig.getUsername();
        return ResponseEntity.ok(username);
    }

    /***
     *
     * @param authenticationRequest
     * ex. {
     *     "username": "avinash",
     *     "password": "avinash123"
     * }
     * @return jwt token
     * @throws Exception In case of bad credentials
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
