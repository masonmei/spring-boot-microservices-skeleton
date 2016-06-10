package com.igitras.uaa.web.rest;

import static com.igitras.common.utils.Constants.Authority.ADMIN;
import static com.igitras.common.utils.Constants.Constrains.LOGIN_REGEX;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.codahale.metrics.annotation.Timed;
import com.igitras.common.utils.HeaderUtil;
import com.igitras.common.utils.PaginationUtil;
import com.igitras.uaa.domain.entity.Authority;
import com.igitras.uaa.domain.entity.User;
import com.igitras.uaa.domain.repository.AuthorityRepository;
import com.igitras.uaa.domain.repository.UserRepository;
import com.igitras.uaa.service.MailService;
import com.igitras.uaa.service.UserService;
import com.igitras.uaa.web.dto.ManagedUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing users.
 *
 * @author mason
 */
@RestController
@RequestMapping("api/users")
public class UserResource {
    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;


    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserDTO the user to create
     * @param request        the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntaxt is incorrect
     */
    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    @Timed
    @Secured(ADMIN)
    public ResponseEntity<?> createUser(@RequestBody ManagedUserDto managedUserDTO, HttpServletRequest request)
            throws URISyntaxException {
        LOG.debug("REST request to save User : {}", managedUserDTO);

        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(managedUserDTO.getLogin()
                .toLowerCase())
                .isPresent()) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
                    .body(null);
        } else if (userRepository.findOneByEmail(managedUserDTO.getEmail())
                .isPresent()) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                    .body(null);
        } else {
            User newUser = userService.createUser(managedUserDTO);
            String baseUrl = request.getScheme() + // "http"
                    "://" +                                // "://"
                    request.getServerName() +              // "myhost"
                    ":" +                                  // ":"
                    request.getServerPort() +              // "80"
                    request.getContextPath();              // "/myContextPath" or "" if deployed in root context
            mailService.sendCreationEmail(newUser, baseUrl);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                    .headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin()))
                    .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldnt be updated
     */
    @RequestMapping(method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    @Secured(ADMIN)
    public ResponseEntity<ManagedUserDto> updateUser(@RequestBody ManagedUserDto managedUserDTO) {
        LOG.debug("REST request to update User : {}", managedUserDTO);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get()
                .getId()
                .equals(managedUserDTO.getId()))) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use"))
                    .body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserDTO.getLogin()
                .toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get()
                .getId()
                .equals(managedUserDTO.getId()))) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
                    .body(null);
        }
        return userRepository.findOneById(managedUserDTO.getId())
                .map(user -> {
                    user.setLogin(managedUserDTO.getLogin());
                    user.setFirstName(managedUserDTO.getFirstName());
                    user.setLastName(managedUserDTO.getLastName());
                    user.setEmail(managedUserDTO.getEmail());
                    user.setActivated(managedUserDTO.isActivated());
                    user.setLangKey(managedUserDTO.getLangKey());
                    Set<Authority> authorities = user.getAuthorities();
                    authorities.clear();
                    managedUserDTO.getAuthorities()
                            .stream()
                            .forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
                    return ResponseEntity.ok()
                            .headers(HeaderUtil.createAlert("userManagement.updated", managedUserDTO.getLogin()))
                            .body(new ManagedUserDto(userRepository.findOne(managedUserDTO.getId())));
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldnt be generated
     */
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ManagedUserDto>> getAllUsers(Pageable pageable) throws URISyntaxException {
        Page<User> page = userRepository.findAll(pageable);
        List<ManagedUserDto> managedUserDTOs = page.getContent()
                .stream()
                .map(ManagedUserDto::new)
                .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "{login:" + LOGIN_REGEX + "}", method = GET, produces = APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedUserDto> getUser(@PathVariable String login) {
        LOG.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
                .map(ManagedUserDto::new)
                .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  USER :login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "{login:" + LOGIN_REGEX + "}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @Timed
    @Secured(ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        LOG.debug("REST request to delete User: {}", login);
        userService.deleteUserInformation(login);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("userManagement.deleted", login))
                .build();
    }
}
