package com.community.userservice.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.community.userservice.dto.RegisterRequest;
import com.community.userservice.entity.Menu;
import com.community.userservice.entity.Role;
import com.community.userservice.entity.User;
import com.community.userservice.service.MenuService;
import com.community.userservice.service.RoleMenuPermService;
import com.community.userservice.service.RoleService;
import com.community.userservice.service.UserRolePermService;
import com.community.userservice.service.UserService;
import com.community.userservice.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final UserRolePermService userRolePermService;
    private final MenuService menuService;
    private final RoleMenuPermService roleMenuPermService;

    @Autowired
    public AuthController(UserService userService, RedisTemplate<String, Object> redisTemplate, 
                         JwtUtil jwtUtil, RoleService roleService, UserRolePermService userRolePermService,
                         MenuService menuService, RoleMenuPermService roleMenuPermService) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
        this.userRolePermService = userRolePermService;
        this.menuService = menuService;
        this.roleMenuPermService = roleMenuPermService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("收到用户注册请求: username={}, email={}, password=******", 
                    request.getUsername(), request.getEmail());
        try {
            User registeredUser = userService.register(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("user", registeredUser);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(String ss) {
        System.out.println("我执行了");
        return "Hello World";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
    	
       System.out.println("****************************************&&&&&&&&&&&&&&&&&&&&&&&&&&");
       
       
       
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            logger.info("收到用户登录请求: username={}, password=******", username);

            String token = userService.login(username, password);
            
            String redisKey = "token:" + username;
            redisTemplate.opsForValue().set(redisKey, token, 86400, TimeUnit.SECONDS);
            logger.info("Token已存入Redis: key={}, value={}", redisKey, token);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");
            
            if (username == null || username.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "用户名不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            if (password == null || password.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "密码不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(username.trim());
            registerRequest.setPassword(password.trim());
            registerRequest.setConfirmPassword(password.trim());
            registerRequest.setEmail(email);
            
            userService.register(registerRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户创建成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("创建用户失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userService.findAll();
        
        
        
        List<Map<String,Object>> list_map=new ArrayList<Map<String,Object>>();
        
        for(User user:users) {
        	
        	
			/*
			 * Date date=new Date();
			 * 
			 * SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
			 * ss=sdf.format(date);
			 */
        	
           LocalDateTime localDateTime=user.getAddtime();
           
           DateTimeFormatter sdf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           
           String ss=localDateTime != null ? localDateTime.format(sdf) : "-";
           
           Map<String,Object> map=new HashMap<String, Object>();
           
           map.put("email", user.getEmail());
           map.put("rowguid", user.getRowguid());
           map.put("username", user.getUsername());
           map.put("addtime", ss);
           
           List<Role> roles = userRolePermService.getRolesByUserGuid(user.getRowguid());
           List<String> roleNames = new ArrayList<>();
           List<Map<String, Object>> roleList = new ArrayList<>();
           for (Role role : roles) {
               roleNames.add(role.getRolename());
               Map<String, Object> roleMap = new HashMap<>();
               roleMap.put("rowguid", role.getRowguid());
               roleMap.put("rolename", role.getRolename());
               roleList.add(roleMap);
           }
           map.put("roles", roleNames);
           map.put("roleList", roleList);
           
           list_map.add(map);
        	
        	
        	
        	
        	
        }
        
        
        
        
        return ResponseEntity.ok(list_map);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");
            
            User updatedUser = userService.update(id, username, password, email);
            
            if (updatedUser != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "用户更新成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }
        } catch (RuntimeException e) {
            logger.error("更新用户失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userRolePermService.clearUserRoles(id);
            userService.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("删除用户失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/users/{id}/roles")
    public ResponseEntity<?> assignRoleToUser(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            String rolelguid = request.get("rolelguid");
            if (rolelguid == null || rolelguid.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "角色ID不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            userRolePermService.assignRoleToUser(id, rolelguid.trim());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "角色分配成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("分配角色失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/users/{id}/roles/{roleId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable String id, @PathVariable String roleId) {
        try {
            userRolePermService.removeRoleFromUser(id, roleId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "角色移除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("移除角色失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/menus")
    public ResponseEntity<List<Map<String, Object>>> getAllMenus() {
        List<Menu> menus = menuService.findAll();
        List<Map<String, Object>> listMap = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (Menu menu : menus) {
            Map<String, Object> map = new HashMap<>();
            map.put("rowguid", menu.getRowguid());
            map.put("menuname", menu.getMenuname());
            map.put("menuicon", menu.getMenuicon());
            map.put("menupage", menu.getMenupage());
            map.put("isroot", menu.getIsroot());
            map.put("parentguid", menu.getParentguid());
            map.put("ordernum", menu.getOrdernum());
            map.put("addtime", menu.getAddtime() != null ? menu.getAddtime().format(sdf) : "-");
            listMap.add(map);
        }
        return ResponseEntity.ok(listMap);
    }

    @GetMapping("/menus/tree")
    public ResponseEntity<List<Menu>> getMenuTree() {
        List<Menu> menuTree = menuService.findTree();
        return ResponseEntity.ok(menuTree);
    }

    @PostMapping("/menus")
    public ResponseEntity<?> createMenu(@RequestBody Map<String, Object> request) {
        try {
            String menuname = (String) request.get("menuname");
            if (menuname == null || menuname.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "菜单名称不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            Menu menu = new Menu();
            menu.setMenuname(menuname.trim());
            menu.setMenuicon((String) request.get("menuicon"));
            menu.setMenupage((String) request.get("menupage"));
            menu.setIsroot((String) request.get("isroot"));
            menu.setParentguid((String) request.get("parentguid"));
            
            Integer ordernum = (Integer) request.get("ordernum");
            menu.setOrdernum(ordernum != null ? ordernum : 0);
            
            menuService.create(menu);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "菜单创建成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("创建菜单失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/menus/{id}")
    public ResponseEntity<?> updateMenu(@PathVariable String id, @RequestBody Map<String, Object> request) {
        try {
            Menu menu = new Menu();
            menu.setMenuname((String) request.get("menuname"));
            menu.setMenuicon((String) request.get("menuicon"));
            menu.setMenupage((String) request.get("menupage"));
            menu.setIsroot((String) request.get("isroot"));
            menu.setParentguid((String) request.get("parentguid"));
            
            Integer ordernum = (Integer) request.get("ordernum");
            menu.setOrdernum(ordernum);
            
            Menu updatedMenu = menuService.update(id, menu);
            
            if (updatedMenu != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "菜单更新成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "菜单不存在");
                return ResponseEntity.ok(response);
            }
        } catch (RuntimeException e) {
            logger.error("更新菜单失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/menus/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable String id) {
        try {
            menuService.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "菜单删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("删除菜单失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/roles/{id}/menus")
    public ResponseEntity<List<String>> getRoleMenus(@PathVariable String id) {
        List<String> menuGuids = roleMenuPermService.getMenuGuidsByRole(id);
        return ResponseEntity.ok(menuGuids);
    }

    @PostMapping("/roles/{id}/menus")
    public ResponseEntity<?> assignRoleMenus(@PathVariable String id, @RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<String> menuGuids = (List<String>) request.get("menuGuids");
            roleMenuPermService.assignMenus(id, menuGuids);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "菜单权限分配成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("分配菜单权限失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/roles/{id}/menus")
    public ResponseEntity<?> removeAllRoleMenus(@PathVariable String id) {
        try {
            roleMenuPermService.removeAllMenus(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "已移除角色所有菜单权限");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("移除角色菜单权限失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/roles/{id}/menus/{menuId}")
    public ResponseEntity<?> removeRoleMenu(@PathVariable String id, @PathVariable String menuId) {
        try {
            roleMenuPermService.removeMenu(id, menuId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "菜单权限移除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("移除菜单权限失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/users/{id}/menus")
    public ResponseEntity<List<Map<String, Object>>> getUserMenus(@PathVariable String id) {
        List<Menu> menus = menuService.findMenusByUserGuid(id);
        List<Map<String, Object>> listMap = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (Menu menu : menus) {
            Map<String, Object> map = new HashMap<>();
            map.put("rowguid", menu.getRowguid());
            map.put("menuname", menu.getMenuname());
            map.put("menuicon", menu.getMenuicon());
            map.put("menupage", menu.getMenupage());
            map.put("isroot", menu.getIsroot());
            map.put("parentguid", menu.getParentguid());
            map.put("ordernum", menu.getOrdernum());
            map.put("addtime", menu.getAddtime() != null ? menu.getAddtime().format(sdf) : "-");
            
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                List<Map<String, Object>> childrenList = new ArrayList<>();
                for (Menu child : menu.getChildren()) {
                    Map<String, Object> childMap = new HashMap<>();
                    childMap.put("rowguid", child.getRowguid());
                    childMap.put("menuname", child.getMenuname());
                    childMap.put("menuicon", child.getMenuicon());
                    childMap.put("menupage", child.getMenupage());
                    childMap.put("isroot", child.getIsroot());
                    childMap.put("parentguid", child.getParentguid());
                    childMap.put("ordernum", child.getOrdernum());
                    childrenList.add(childMap);
                }
                map.put("children", childrenList);
            }
            
            listMap.add(map);
        }
        return ResponseEntity.ok(listMap);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("message", "未提供有效Token");
                return ResponseEntity.ok(response);
            }
            
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            String redisKey = "token:" + username;
            String storedToken = (String) redisTemplate.opsForValue().get(redisKey);
            
            if (storedToken != null && storedToken.equals(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);
                
                List<User> users = userService.findAll();
                User currentUser = users.stream()
                        .filter(u -> username.equals(u.getUsername()))
                        .findFirst()
                        .orElse(null);
                if (currentUser != null) {
                    response.put("userid", currentUser.getRowguid());
                }
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("message", "Token已过期或不存在");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("Token验证失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Token格式无效");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                String redisKey = "token:" + username;
                redisTemplate.delete(redisKey);
                logger.info("用户退出登录: username={}", username);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "退出登录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("退出登录失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "退出登录失败");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Map<String, Object>>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        List<Map<String, Object>> listMap = new ArrayList<>();
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (Role role : roles) {
            Map<String, Object> map = new HashMap<>();
            map.put("rowguid", role.getRowguid());
            map.put("rolename", role.getRolename());
            map.put("addtime", role.getAddtime() != null ? role.getAddtime().format(sdf) : "-");
            listMap.add(map);
        }
        return ResponseEntity.ok(listMap);
    }

    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody Map<String, String> request) {
        try {
            String rolename = request.get("rolename");
            if (rolename == null || rolename.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "角色名称不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            Role role = new Role();
            role.setRolename(rolename.trim());
            roleService.create(role);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "角色创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("创建角色失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建角色失败");
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<?> updateRole(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            String rolename = request.get("rolename");
            if (rolename == null || rolename.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "角色名称不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            Role role = new Role();
            role.setRolename(rolename.trim());
            Role updatedRole = roleService.update(id, role);
            
            if (updatedRole != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "角色更新成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "角色不存在");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            logger.error("更新角色失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新角色失败");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable String id) {
        try {
            roleService.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "角色删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("删除角色失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除角色失败");
            return ResponseEntity.ok(response);
        }
    }
}